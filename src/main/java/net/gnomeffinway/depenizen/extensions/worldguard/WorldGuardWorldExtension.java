package net.gnomeffinway.depenizen.extensions.worldguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.worldguard.WorldGuardRegion;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.plugins.WorldGuardSupport;
import org.bukkit.World;

public class WorldGuardWorldExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dWorld;
    }

    public static WorldGuardWorldExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldGuardWorldExtension((dWorld) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    private WorldGuardWorldExtension(dWorld world) {
        this.world = world.getWorld();
        this.manager = ((WorldGuardPlugin) Support.getPlugin(WorldGuardSupport.class))
                .getRegionManager(this.world);
    }

    World world;
    RegionManager manager;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <w@world.list_regions>
        // @returns dList(Region)
        // @description
        // Returns a list of WorldGuard regions in this world.
        // @plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("list_regions")) {
            dList regions = new dList();
            for (ProtectedRegion r : manager.getRegions().values()) {
                regions.add(new WorldGuardRegion(r, world).identify());
            }
            return regions.getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
