package com.denizenscript.depenizen.bukkit.extensions.worldguard;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
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

    public static final String[] handledTags = new String[] {
            "list_regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldGuardWorldExtension(dWorld world) {
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
        // @Plugin DepenizenBukkit, WorldGuard
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
