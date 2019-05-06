package com.denizenscript.depenizen.bukkit.extensions.worldguard;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.WorldGuardSupport;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
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

    public static final String[] handledTags = new String[] {
            "has_region", "regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldGuardCuboidExtension(dCuboid cuboid) {
        this.cuboid = cuboid;
    }

    dCuboid cuboid = null;

    private ApplicableRegionSet getApplicableRegions() {
        WorldGuardPlugin worldGuard = Support.getPlugin(WorldGuardSupport.class);
        dLocation low = cuboid.getLow(0);
        dLocation high = cuboid.getHigh(0);
        BlockVector3 vecLow = BlockVector3.at(low.getX(), low.getY(), low.getZ());
        BlockVector3 vecHigh = BlockVector3.at(high.getX(), high.getY(), high.getZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("FAKE_REGION", vecLow, vecHigh);
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(cuboid.getWorld())).getApplicableRegions(region);
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
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("has_region")) {
            return new Element(hasRegion()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <cu@cuboid.regions>
        // @returns dList(Region)
        // @description
        // Returns a list of regions that are in this cuboid.
        // @Plugin DepenizenBukkit, WorldGuard
        // -->
        if (attribute.startsWith("regions")) {
            return getRegions(cuboid.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
