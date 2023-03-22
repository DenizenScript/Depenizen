package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.denizenscript.depenizen.bukkit.bridges.WorldGuardBridge;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.World;

public class WorldGuardCuboidProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldGuardCuboid";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag cuboid) {
        return cuboid instanceof CuboidTag;
    }

    public static WorldGuardCuboidProperties getFrom(ObjectTag cuboid) {
        if (!describes(cuboid)) {
            return null;
        }
        else {
            return new WorldGuardCuboidProperties((CuboidTag) cuboid);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_region", "regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public WorldGuardCuboidProperties(CuboidTag cuboid) {
        this.cuboid = cuboid;
    }

    CuboidTag cuboid;

    public ApplicableRegionSet getApplicableRegions() {
        WorldGuardPlugin worldGuard = (WorldGuardPlugin) WorldGuardBridge.instance.plugin;
        LocationTag low = cuboid.getLow(0);
        LocationTag high = cuboid.getHigh(0);
        BlockVector3 vecLow = BlockVector3.at(low.getX(), low.getY(), low.getZ());
        BlockVector3 vecHigh = BlockVector3.at(high.getX(), high.getY(), high.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("FAKE_REGION", vecLow, vecHigh);
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(cuboid.getWorld().getWorld())).getApplicableRegions(region);
    }

    public boolean hasRegion() {
        return getApplicableRegions().size() > 0;
    }

    public ListTag getRegions(World world) {
        ListTag regionList = new ListTag();
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            regionList.addObject(new WorldGuardRegionTag(protectedRegion, world));
        }
        return regionList;
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <CuboidTag.has_region>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns whether the cuboid contains any region.
        // -->
        if (attribute.startsWith("has_region")) {
            return new ElementTag(hasRegion()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <CuboidTag.regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of regions that are in this cuboid.
        // -->
        if (attribute.startsWith("regions")) {
            return getRegions(cuboid.getWorld().getWorld()).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
