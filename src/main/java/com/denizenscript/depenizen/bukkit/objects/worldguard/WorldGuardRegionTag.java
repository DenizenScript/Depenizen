package com.denizenscript.depenizen.bukkit.objects.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
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

    // <--[language]
    // @name WorldGuardRegionTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, WorldGuard
    // @description
    // A WorldGuardRegionTag represents a WorldGuard region in the world.
    //
    // These use the object notation "region@".
    // The identity format for regions is <region_name>,<world_name>
    // For example, 'region@spawnland,Hub'.
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
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
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

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <WorldGuardRegionTag.cuboid>
        // @returns CuboidTag
        // @group conversion
        // @plugin Depenizen, WorldGuard
        // @description
        // Converts a cuboid-shaped region to a CuboidTag.
        // -->
        if (attribute.startsWith("cuboid")) {
            if (!(region instanceof ProtectedCuboidRegion)) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("<WorldGuardRegionTag.cuboid> requires a Cuboid-shaped region!");
                }
                return null;
            }
            return new CuboidTag(BukkitAdapter.adapt(world, region.getMinimumPoint()),
                    BukkitAdapter.adapt(world, region.getMaximumPoint())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.id>
        // @returns ElementTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the ID name of the region.
        // -->
        if (attribute.startsWith("id")) {
            return new ElementTag(region.getId()).getAttribute(attribute.fulfill(1));
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
            return list.getAttribute(attribute.fulfill(1));
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
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <WorldGuardRegionTag.world>
        // @returns WorldTag
        // @plugin Depenizen, WorldGuard
        // @description
        // Gets the WorldTag this region is in.
        // -->
        if (attribute.startsWith("world")) {
            return new WorldTag(world).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}
