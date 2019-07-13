package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
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

    public static boolean describes(ObjectTag object) {
        return object instanceof WorldTag;
    }

    public static WorldGuardWorldProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new WorldGuardWorldProperties((WorldTag) object);
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

    private WorldGuardWorldProperties(WorldTag world) {
        this.world = world.getWorld();
        this.manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(this.world));
    }

    World world;
    RegionManager manager;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <WorldTag.list_regions>
        // @returns ListTag(Region)
        // @description
        // Returns a list of WorldGuard regions in this world.
        // @Plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("list_regions")) {
            ListTag regions = new ListTag();
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
