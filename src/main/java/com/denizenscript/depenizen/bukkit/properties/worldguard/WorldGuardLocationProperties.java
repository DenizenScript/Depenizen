package com.denizenscript.depenizen.bukkit.properties.worldguard;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.worldguard.WorldGuardRegionTag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.World;

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
        return loc instanceof LocationTag;
    }

    public static WorldGuardLocationProperties getFrom(ObjectTag loc) {
        if (!describes(loc)) {
            return null;
        }
        else {
            return new WorldGuardLocationProperties((LocationTag) loc);
        }
    }

    public static final String[] handledTags = new String[] {
            "in_region", "regions"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public WorldGuardLocationProperties(LocationTag loc) {
        location = loc;
    }

    LocationTag location;

    public ApplicableRegionSet getApplicableRegions() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()))
                .getApplicableRegions(BukkitAdapter.asBlockVector(location));
    }

    public boolean inRegion() {
        return getApplicableRegions().size() > 0;
    }

    public boolean inRegion(String region) {
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            if (protectedRegion.getId().equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
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
        // @attribute <LocationTag.in_region[(<name>|...)]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, WorldGuard
        // @description
        // If a region name or list of names is specified, returns whether the
        // location is in one of the listed regions, otherwise returns whether
        // the location is in any region.
        // -->
        if (attribute.startsWith("in_region")) {
            // Check if the location is in the specified region
            if (attribute.hasParam()) {
                ListTag region_list = attribute.paramAsType(ListTag.class);
                for (String region : region_list) {
                    if (inRegion(region)) {
                        return new ElementTag(true).getObjectAttribute(attribute.fulfill(1));
                    }
                }
                return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
            }

            // Check if the location is in any region
            else {
                return new ElementTag(inRegion()).getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <LocationTag.regions>
        // @returns ListTag(WorldGuardRegionTag)
        // @plugin Depenizen, WorldGuard
        // @description
        // Returns a list of regions that the location is in.
        // -->
        if (attribute.startsWith("regions")) {
            return getRegions(location.getWorld()).getObjectAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
