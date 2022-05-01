package com.denizenscript.depenizen.bukkit.objects.worldguard;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.denizencore.utilities.debugging.Warning;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldGuardRegionTag implements ObjectTag {

    // <--[ObjectType]
    // @name WorldGuardRegionTag
    // @prefix region
    // @base ElementTag
    // @format
    // The identity format for regions is <region_name>,<world_name>
    // For example, 'region@spawnland,Hub'.
    //
    // @plugin Depenizen, WorldGuard
    // @description
    // A WorldGuardRegionTag represents a WorldGuard region in the world.
    //
    // -->

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern regionPattern = Pattern.compile("(?:region@)?(.+),(.+)", Pattern.CASE_INSENSITIVE);

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static WorldGuardRegionTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("region")
    public static WorldGuardRegionTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        Matcher m = regionPattern.matcher(string);
        if (m.matches()) {
            String regionName = m.group(1);
            String worldName = m.group(2);
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                Debug.echoError("valueOf WorldGuard region returning null: Invalid world '" + worldName + "'");
                return null;
            }
            RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (!manager.hasRegion(regionName)) {
                Debug.echoError("valueOf WorldGuard region returning null: Invalid region '" + regionName
                        + "' for world '" + worldName + "'");
                return null;
            }
            return new WorldGuardRegionTag(manager.getRegion(regionName), world);
        }

        return null;
    }

    public static boolean matches(String arg) {
        return regionPattern.matcher(arg).matches();
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    ProtectedRegion region;
    World world;

    public WorldGuardRegionTag(ProtectedRegion region, World world) {
        this.region = region;
        this.world = world;
    }

    /////////////////////
    //   INSTANCE FIELDS/METHODS
    /////////////////

    public ProtectedRegion getRegion() {
        return region;
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Region";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "WorldGuardRegion";
    }

    @Override
    public String identify() {
        return "region@" + region.getId() + "," + world.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    public static Warning oldCuboidTag = new SlowWarning("worldguardregionCuboid", "The tag 'WorldGuardRegionTag.cuboid' is deprecated in favor of the '.area' equivalent.");

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <WorldGuardRegionTag.area>
        // @returns AreaObject
        // @group conversion
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the region's block area as a CuboidTag or PolygonTag.
        // -->
        if (attribute.startsWith("area")) {
            try {
                if (region instanceof ProtectedPolygonalRegion) {
                    ProtectedPolygonalRegion polyRegion = ((ProtectedPolygonalRegion) region);
                    PolygonTag poly = new PolygonTag(new WorldTag(world));
                    for (BlockVector2 vec2 : polyRegion.getPoints()) {
                        poly.corners.add(new PolygonTag.Corner(vec2.getX(), vec2.getZ()));
                    }
                    poly.yMin = polyRegion.getMinimumPoint().getY();
                    poly.yMax = polyRegion.getMaximumPoint().getY();
                    poly.recalculateBox();
                    return poly.getObjectAttribute(attribute.fulfill(1));
                }
                return new CuboidTag(BukkitAdapter.adapt(world, region.getMinimumPoint()),
                        BukkitAdapter.adapt(world, region.getMaximumPoint())).getObjectAttribute(attribute.fulfill(1));
            }
            catch (Throwable ex) {
                Debug.echoError(ex);
            }
        }

        if (attribute.startsWith("cuboid")) {
            oldCuboidTag.warn(attribute.context);
            if (!(region instanceof ProtectedCuboidRegion)) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("<WorldGuardRegionTag.cuboid> requires a Cuboid-shaped region!");
                }
                return null;
            }
            return new CuboidTag(BukkitAdapter.adapt(world, region.getMinimumPoint()),
                    BukkitAdapter.adapt(world, region.getMaximumPoint())).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.id>
        // @returns ElementTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the ID name of the region.
        // -->
        if (attribute.startsWith("id")) {
            return new ElementTag(region.getId()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.parent>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the parent region of this region (if any).
        // -->
        if (attribute.startsWith("parent")) {
            ProtectedRegion parent = region.getParent();
            if (parent == null) {
                return null;
            }
            return new WorldGuardRegionTag(parent, world).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.children>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all children of this region.
        // -->
        if (attribute.startsWith("children")) {
            ListTag children = new ListTag();
            RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            for (ProtectedRegion child : manager.getRegions().values()) {
                if (child.getParent() == region) {
                    children.addObject(new WorldGuardRegionTag(child, world));
                }
            }
            return children.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all members of a region. (Members are permitted to build, etc.)
        // -->
        if (attribute.startsWith("members")) {
            ListTag list = new ListTag();
            for (UUID uuid : region.getMembers().getUniqueIds()) {
                list.addObject(PlayerTag.mirrorBukkitPlayer(Bukkit.getOfflinePlayer(uuid)));
            }
            return list.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.owners>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all owners of a region. (Owners are permitted to build, edit settings, etc.)
        // -->
        if (attribute.startsWith("owners")) {
            ListTag list = new ListTag();
            for (UUID uuid : region.getOwners().getUniqueIds()) {
                list.addObject(PlayerTag.mirrorBukkitPlayer(Bukkit.getOfflinePlayer(uuid)));
            }
            return list.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.world>
        // @returns WorldTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the WorldTag this region is in.
        // -->
        if (attribute.startsWith("world")) {
            return new WorldTag(world).getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);

    }
}
