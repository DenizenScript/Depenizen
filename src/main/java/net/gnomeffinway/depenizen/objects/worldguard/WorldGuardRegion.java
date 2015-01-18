package net.gnomeffinway.depenizen.objects.worldguard;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.plugins.WorldGuardSupport;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorldGuardRegion implements dObject {

    /////////////////////
    //   PATTERNS
    /////////////////

    final static Pattern regionPattern = Pattern.compile("(?:region@)?(.+),(.+)", Pattern.CASE_INSENSITIVE);

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("region")
    public static WorldGuardRegion valueOf(String string) {
        if (string == null) return null;

        Matcher m = regionPattern.matcher(string);
        if (m.matches()) {
            String regionName = m.group(1);
            String worldName = m.group(2);
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                dB.echoError("valueOf WorldGuard region returning null: Invalid world '" + worldName + "'");
                return null;
            }
            WorldGuardPlugin plugin = Support.getPlugin(WorldGuardSupport.class);
            RegionManager manager = plugin.getRegionManager(world);
            if (!manager.hasRegion(regionName)) {
                dB.echoError("valueOf WorldGuard region returning null: Invalid region '" + regionName
                        + "' for world '" + worldName + "'");
                return null;
            }
            return new WorldGuardRegion(manager.getRegion(regionName), world);
        }

        return null;
    }

    public static boolean matches(String arg) {
        return regionPattern.matcher(arg).matches();
    }


    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    ProtectedRegion region = null;
    World world = null;

    public WorldGuardRegion(ProtectedRegion region, World world) {
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
    //   dObject Methods
    /////////////////

    private String prefix = "Region";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <region@region.as_cuboid>
        // @returns dCuboid
        // @group conversion
        // @description
        // Converts a cuboid-shaped region to a dCuboid.
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("as_cuboid")) {
            if (!(region instanceof ProtectedCuboidRegion)) {
                if (!attribute.hasAlternative())
                    dB.echoError("<region@region.as_cuboid> requires a Cuboid-shaped region!");
                return null;
            }
            return new dCuboid(BukkitUtil.toLocation(world, region.getMinimumPoint()),
                    BukkitUtil.toLocation(world, region.getMaximumPoint())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <region@region.id>
        // @returns Element
        // @description
        // Gets the ID name of the region.
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("id")) {
            return new Element(region.getId()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <region@region.members>
        // @returns dList(dPlayer)
        // @description
        // Gets a list of all members of a region. (Members are permitted to build, etc.)
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("members")) {
            dList list = new dList();
            for (String str: region.getMembers().getPlayers()) {
                dPlayer player = dPlayer.valueOf(str);
                if (player != null)
                    list.add(player.identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <region@region.owners>
        // @returns dList(dPlayer)
        // @description
        // Gets a list of all owners of a region. (Owners are permitted to build, edit settings, etc.)
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("owners")) {
            dList list = new dList();
            for (String str: region.getOwners().getPlayers()) {
                dPlayer player = dPlayer.valueOf(str);
                if (player != null)
                    list.add(player.identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <hclass@class.type>
        // @returns Element
        // @description
        // Always returns 'Region' for WorldGuardRegion objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("type")) {
            return new Element("Region").getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <region@region.world>
        // @returns dWorld
        // @description
        // Gets the dWorld this region is in.
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("world")) {
            return new dWorld(world).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }
}
