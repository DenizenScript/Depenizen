package com.denizenscript.depenizen.bukkit.objects.worldguard;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.PolygonTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.denizencore.utilities.debugging.Warning;
import com.denizenscript.depenizen.bukkit.bridges.WorldGuardBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

public class WorldGuardRegionTag implements ObjectTag {

    // <--[ObjectType]
    // @name WorldGuardRegionTag
    // @prefix region
    // @base ElementTag
    // @format
    // The identity format for regions is <region_name>,<world_name>.
    // For example, 'region@spawnland,Hub'.
    //
    // @plugin Depenizen, WorldGuard
    // @description
    // A WorldGuardRegionTag represents a WorldGuard region in the world.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("region")
    public static WorldGuardRegionTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        if (string.startsWith("region@")) {
            string = string.substring("region@".length());
        }
        int lastComma = string.lastIndexOf(',');
        if (lastComma == -1) {
            return null;
        }
        String regionId = string.substring(0, lastComma);
        String worldName = string.substring(lastComma + 1);
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            if (context == null || context.showErrors()) {
                Debug.echoError("valueOf WorldGuardRegionTag returning null: Invalid world '" + worldName + "'.");
            }
            return null;
        }
        if (!WorldGuardBridge.getManager(world).hasRegion(regionId)) {
            if (context == null || context.showErrors()) {
                Debug.echoError("valueOf WorldGuardRegionTag returning null: Invalid region '" + regionId + "' for world '" + worldName + "'.");
            }
            return null;
        }
        return new WorldGuardRegionTag(regionId, world);
    }

    public static boolean matches(String arg) {
        if (arg.startsWith("region@")) {
            return true;
        }
        return valueOf(arg, CoreUtilities.noDebugContext) != null;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    String regionId;
    World world;

    public WorldGuardRegionTag(ProtectedRegion region, World world) {
        this(region.getId(), world);
    }

    public WorldGuardRegionTag(String regionId, World world) {
        this.regionId = regionId;
        this.world = world;
    }

    /////////////////////
    //   INSTANCE FIELDS/METHODS
    /////////////////

    public ProtectedRegion getRegion() {
        return WorldGuardBridge.getManager(world).getRegion(regionId);
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
    public String identify() {
        return "region@" + regionId + "," + world.getName();
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

    public static void register() {

        // <--[tag]
        // @attribute <WorldGuardRegionTag.area>
        // @returns AreaObject
        // @group conversion
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns the region's block area as a CuboidTag or PolygonTag.
        // -->
        tagProcessor.registerTag(ObjectTag.class, "area", (attribute, object) -> {
            ProtectedRegion region = object.getRegion();
            if (region.getType() == RegionType.POLYGON) {
                ProtectedPolygonalRegion polyRegion = ((ProtectedPolygonalRegion) region);
                PolygonTag poly = new PolygonTag(new WorldTag(object.world));
                for (BlockVector2 corner : polyRegion.getPoints()) {
                    poly.corners.add(new PolygonTag.Corner(corner.getX(), corner.getZ()));
                }
                poly.yMin = polyRegion.getMinimumPoint().getY();
                poly.yMax = polyRegion.getMaximumPoint().getY();
                poly.recalculateBox();
                return poly;
            }
            return new CuboidTag(BukkitAdapter.adapt(object.world, region.getMinimumPoint()), BukkitAdapter.adapt(object.world, region.getMaximumPoint()));
        });

        tagProcessor.registerTag(CuboidTag.class, "cuboid", (attribute, object) -> {
            oldCuboidTag.warn(attribute.context);
            ProtectedRegion region = object.getRegion();
            if (region.getType() != RegionType.CUBOID) {
                attribute.echoError("<WorldGuardRegionTag.cuboid> requires a Cuboid-shaped region!");
                return null;
            }
            return new CuboidTag(BukkitAdapter.adapt(object.world, region.getMinimumPoint()), BukkitAdapter.adapt(object.world, region.getMaximumPoint()));
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.id>
        // @returns ElementTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the ID name of the region.
        // -->
        tagProcessor.registerStaticTag(ElementTag.class, "id", (attribute, object) -> {
            return new ElementTag(object.regionId);
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.parent>
        // @returns WorldGuardRegionTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the parent region of this region (if any).
        // -->
        tagProcessor.registerTag(WorldGuardRegionTag.class, "parent", (attribute, object) -> {
            ProtectedRegion parent = object.getRegion().getParent();
            return parent != null ? new WorldGuardRegionTag(parent, object.world) : null;
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.children>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all children of this region.
        // -->
        tagProcessor.registerTag(ListTag.class, "children", (attribute, object) -> {
            ListTag children = new ListTag();
            ProtectedRegion region = object.getRegion();
            for (ProtectedRegion child : WorldGuardBridge.getManager(object.world).getRegions().values()) {
                if (child.getParent() == region) {
                    children.addObject(new WorldGuardRegionTag(child, object.world));
                }
            }
            return children;
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all members of a region. (Members are permitted to build, etc.)
        // -->
        tagProcessor.registerTag(ListTag.class, "members", (attribute, object) -> {
            ListTag members = new ListTag();
            for (UUID member : object.getRegion().getMembers().getUniqueIds()) {
                members.addObject(new PlayerTag(member));
            }
            return members;
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.owners>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets a list of all owners of a region. (Owners are permitted to build, edit settings, etc.)
        // -->
        tagProcessor.registerTag(ListTag.class, "owners", (attribute, object) -> {
            ListTag owners = new ListTag();
            for (UUID owner : object.getRegion().getOwners().getUniqueIds()) {
                owners.addObject(new PlayerTag(owner));
            }
            return owners;
        });

        // <--[tag]
        // @attribute <WorldGuardRegionTag.world>
        // @returns WorldTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the WorldTag this region is in.
        // -->
        tagProcessor.registerTag(WorldTag.class, "world", (attribute, object) -> {
            return new WorldTag(object.world);
        });
    }

    public static ObjectTagProcessor<WorldGuardRegionTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

}
