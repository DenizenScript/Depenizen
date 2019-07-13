package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.dWorld;
import com.denizenscript.denizencore.objects.dList;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.World;

public class WorldGuardWorldProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldGuardWorld";
    }

    public static boolean describes(dObject object) {
        return object instanceof dWorld;
    }

    public static WorldGuardWorldProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldGuardWorldProperties((dWorld) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "list_regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldGuardWorldProperties(dWorld world) {
        this.world = world.getWorld();
        this.manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(this.world));
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
        // @Plugin Depenizen, WorldGuard
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
