package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.bridges.WorldGuardBridge;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardWorldExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <WorldTag.list_regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of WorldGuard regions in this world.
        // -->
        WorldTag.tagProcessor.registerTag(ListTag.class, "list_regions", (attribute, world) -> {
            ListTag listRegions = new ListTag();
            for (ProtectedRegion region : WorldGuardBridge.getManager(world.getWorld()).getRegions().values()) {
                listRegions.addObject(new WorldGuardRegionTag(region, world.getWorld()));
            }
            return listRegions;
        });

        // <--[tag]
        // @attribute <WorldTag.has_region[<name>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns whether a region exists in this world for the given name.
        // -->
        WorldTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "has_region", (attribute, world, name) -> {
            return new ElementTag(WorldGuardBridge.getManager(world.getWorld()).hasRegion(name.asString()));
        });
    }

}
