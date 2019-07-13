package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.objects.dLocation;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldGuardLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag loc) {
        return loc instanceof dLocation;
    }

    public static WorldGuardLocationProperties getFrom(ObjectTag loc) {
        if (!describes(loc)) {
            return null;
        }
        else {
            return new WorldGuardLocationProperties((dLocation) loc);
        }
    }

    public static final String[] handledTags = new String[] {
            "in_region", "regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private WorldGuardLocationProperties(dLocation loc) {
        location = loc;
    }

    dLocation location = null;

    private ApplicableRegionSet getApplicableRegions() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()))
                .getApplicableRegions(BukkitAdapter.asBlockVector(location));
    }

    private boolean inRegion() {
        return getApplicableRegions().size() > 0;
    }

    private boolean inRegion(String region) {
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            if (protectedRegion.getId().equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
    }

    private ListTag getRegions(World world) {
        List<String> regionList = new ArrayList<>();
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            regionList.add(new WorldGuardRegion(protectedRegion, world).identify());
        }
        return new ListTag(regionList);
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.in_region[<name>|...]>
        // @returns ElementTag(Boolean)
        // @description
        // If a region name or list of names is specified, returns whether the
        // location is in one of the listed regions, otherwise returns whether
        // the location is in any region.
        // @Plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("in_region")) {
            // Check if the location is in the specified region
            if (attribute.hasContext(1)) {
                ListTag region_list = ListTag.valueOf(attribute.getContext(1));
                for (String region : region_list) {
                    if (inRegion(region)) {
                        return new ElementTag(true).getAttribute(attribute.fulfill(1));
                    }
                }
                return new ElementTag(false).getAttribute(attribute.fulfill(1));
            }

            // Check if the location is in any region
            else {
                return new ElementTag(inRegion()).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <l@location.regions>
        // @returns ListTag(Region)
        // @description
        // Returns a list of regions that the location is in.
        // @Plugin Depenizen, WorldGuard
        // -->
        if (attribute.startsWith("regions")) {
            return getRegions(location.getWorld()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
