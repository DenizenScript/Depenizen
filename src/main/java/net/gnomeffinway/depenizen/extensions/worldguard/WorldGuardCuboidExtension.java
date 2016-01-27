package net.gnomeffinway.depenizen.extensions.worldguard;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.worldguard.WorldGuardRegion;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.support.plugins.WorldGuardSupport;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardCuboidExtension extends dObjectExtension {

    public static boolean describes(dObject cuboid) {
        return cuboid instanceof dCuboid;
    }

    public static WorldGuardCuboidExtension getFrom(dObject cuboid) {
        if (!describes(cuboid)) {
            return null;
        }
        else {
            return new WorldGuardCuboidExtension((dCuboid) cuboid);
        }
    }

    private WorldGuardCuboidExtension(dCuboid cuboid) {
        this.cuboid = cuboid;
    }

    dCuboid cuboid = null;

    private ApplicableRegionSet getApplicableRegions() {
        WorldGuardPlugin worldGuard = Support.getPlugin(WorldGuardSupport.class);
        dLocation low = cuboid.getLow(0);
        dLocation high = cuboid.getHigh(0);
        BlockVector vecLow = new BlockVector(low.getX(), low.getY(), low.getZ());
        BlockVector vecHigh = new BlockVector(high.getX(), high.getY(), high.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("FAKE_REGION", vecLow, vecHigh);
        return worldGuard.getRegionManager(cuboid.getWorld()).getApplicableRegions(region);
    }

    private boolean hasRegion() {
        return getApplicableRegions().size() > 0;
    }

    private dList getRegions(World world) {
        List<String> regionList = new ArrayList<String>();
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            regionList.add(new WorldGuardRegion(protectedRegion, world).identify());
        }
        return new dList(regionList);
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <cu@cuboid.has_region>
        // @returns Element(Boolean)
        // @description
        // Returns whether the cuboid contains any region.
        // @Plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("has_region")) {
            return new Element(hasRegion()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <cu@cuboid.regions>
        // @returns dList(Region)
        // @description
        // Returns a list of regions that are in this cuboid.
        // @Plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("regions")) {
            return getRegions(cuboid.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
