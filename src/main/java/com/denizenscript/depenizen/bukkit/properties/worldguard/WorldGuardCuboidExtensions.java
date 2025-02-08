package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WorldGuardCuboidExtensions {

    public static ApplicableRegionSet getApplicableRegions(CuboidTag cuboid) {
        LocationTag low = cuboid.getLow(0);
        LocationTag high = cuboid.getHigh(0);
        BlockVector3 vecLow = BlockVector3.at(low.getX(), low.getY(), low.getZ());
        BlockVector3 vecHigh = BlockVector3.at(high.getX(), high.getY(), high.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("FAKE_REGION", vecLow, vecHigh);
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(cuboid.getWorld().getWorld())).getApplicableRegions(region);
    }

    public static void register() {

        // <--[tag]
        // @attribute <CuboidTag.has_region>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @deprecated Use 'CuboidTag.worldguard_has_region'
        // @description
        // Deprecated in favor of <@link tag CuboidTag.worldguard_has_region>.
        // -->

        // <--[tag]
        // @attribute <CuboidTag.worldguard_has_region>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns whether the cuboid contains any region.
        // -->
        CuboidTag.tagProcessor.registerTag(ElementTag.class, "worldguard_has_region", (attribute, area) -> {
            return new ElementTag(getApplicableRegions(area).size() > 0);
        }, "has_region");

        // <--[tag]
        // @attribute <CuboidTag.regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @deprecated Use 'CuboidTag.worldguard_regions'
        // @description
        // Deprecated in favor of <@link tag CuboidTag.worldguard_regions>.
        // -->

        // <--[tag]
        // @attribute <CuboidTag.worldguard_regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of regions that are in this cuboid.
        // -->
        CuboidTag.tagProcessor.registerTag(ListTag.class, "worldguard_regions", (attribute, area) -> {
            ListTag regionList = new ListTag();
            for (ProtectedRegion protectedRegion : getApplicableRegions(area)) {
                regionList.addObject(new WorldGuardRegionTag(protectedRegion, area.getWorld().getWorld()));
            }
            return regionList;
        }, "regions");
    }

}
