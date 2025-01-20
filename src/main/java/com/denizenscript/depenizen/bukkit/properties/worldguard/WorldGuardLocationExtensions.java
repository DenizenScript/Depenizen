package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.Set;

public class WorldGuardLocationExtensions {

    public static Set<ProtectedRegion> getApplicableRegions(LocationTag loc) {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(loc)).getRegions();
    }

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.in_region[(<name>|...)]>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @deprecated Use 'LocationTag.worldguard_in_region[(<name>|...)]'
        // @description
        // Deprecated in favor of <@link tag LocationTag.worldguard_in_region[(<name>|...)]>.
        // -->

        // <--[tag]
        // @attribute <LocationTag.worldguard_in_region[(<name>|...)]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // If a region name or list of names is specified, returns whether the
        // location is in one of the listed regions, otherwise returns whether
        // the location is in any region.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "worldguard_in_region", (attribute, loc) -> {
            Set<ProtectedRegion> queryRegions = getApplicableRegions(loc);
            if (attribute.hasParam()) {
                ListTag regions = attribute.paramAsType(ListTag.class);
                for (ProtectedRegion region : queryRegions) {
                    for (String id : regions) {
                        if (id.equalsIgnoreCase(region.getId())) {
                            return new ElementTag(true);
                        }
                    }
                }
                return new ElementTag(false);
            }
            return new ElementTag(!queryRegions.isEmpty());
        }, "in_region");

        // <--[tag]
        // @attribute <LocationTag.regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @deprecated Use 'LocationTag.worldguard_regions'
        // @description
        // Deprecated in favor of <@link tag LocationTag.worldguard_regions>.
        // -->

        // <--[tag]
        // @attribute <LocationTag.worldguard_regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of regions that the location is in.
        // -->
        LocationTag.tagProcessor.registerTag(ListTag.class, "worldguard_regions", (attribute, loc) -> {
            Set<ProtectedRegion> queryRegions = getApplicableRegions(loc);
            ListTag regions = new ListTag(queryRegions.size());
            for (ProtectedRegion region : queryRegions) {
                regions.addObject(new WorldGuardRegionTag(region, loc.getWorld()));
            }
            return regions;
        }, "regions");
    }

}
