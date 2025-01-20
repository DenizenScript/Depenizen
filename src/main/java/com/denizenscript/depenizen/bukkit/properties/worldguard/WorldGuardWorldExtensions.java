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
        // @deprecated Use 'WorldTag.worldguard_list_regions'
        // @description
        // Deprecated in favor of <@link tag WorldTag.worldguard_list_regions>.
        // -->

        // <--[tag]
        // @attribute <WorldTag.worldguard_list_regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of WorldGuard regions in this world.
        // -->
        WorldTag.tagProcessor.registerTag(ListTag.class, "worldguard_list_regions", (attribute, world) -> {
            ListTag listRegions = new ListTag();
            for (ProtectedRegion region : WorldGuardBridge.getManager(world.getWorld()).getRegions().values()) {
                listRegions.addObject(new WorldGuardRegionTag(region, world.getWorld()));
            }
            return listRegions;
        }, "list_regions");

        // <--[tag]
        // @attribute <WorldTag.has_region[<name>]>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @deprecated Use 'WorldTag.worldguard_has_region[<name>]'
        // @description
        // Deprecated in favor of <@link tag WorldTag.worldguard_has_region[<name>]>.
        // -->

        // <--[tag]
        // @attribute <WorldTag.worldguard_has_region[<name>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns whether a region exists in this world for the given name.
        // -->
        WorldTag.tagProcessor.registerTag(ElementTag.class, ElementTag.class, "worldguard_has_region", (attribute, world, name) -> {
            return new ElementTag(WorldGuardBridge.getManager(world.getWorld()).hasRegion(name.asString()));
        }, "has_region");
    }

}
