package net.gnomeffinway.depenizen.tags.worldguard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.objects.properties.Property;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.Depenizen;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardLocationTags implements Property {

    public static boolean describes(dObject loc) {
        return loc instanceof dLocation;
    }

    public static WorldGuardLocationTags getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new WorldGuardLocationTags((dLocation) loc);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private WorldGuardLocationTags(dLocation loc) {
        location = loc;
    }

    dLocation location = null;

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "WorldGuardLocationTags";
    }

    private ApplicableRegionSet getApplicableRegions() {
        return Depenizen.worldguard.getRegionManager(location.getWorld()).getApplicableRegions(location);
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

    private dList getRegions() {
        List<String> regionList = new ArrayList<String>();
        for (ProtectedRegion protectedRegion : getApplicableRegions()) {
            regionList.add(protectedRegion.getId());
        }
        return new dList(regionList);
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.in_region[<name>|...]>
        // @returns Element(Boolean)
        // @description
        // If a region name or list of names is specified, returns whether the
        // location is in one of the listed regions, otherwise returns whether
        // the location is in any region.
        // @Plugin WorldGuard
        // -->
        if (attribute.startsWith("in_region")) {
            // Check if the location is in the specified region
            if (attribute.hasContext(1)) {
                dList region_list = dList.valueOf(attribute.getContext(1));
                for (String region : region_list) {
                    if (inRegion(region))
                        return Element.TRUE.getAttribute(attribute.fulfill(1));
                }
                return Element.FALSE.getAttribute(attribute.fulfill(1));
            }

            // Check if the location is in any region
            else {
                return new Element(inRegion()).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <l@location.regions>
        // @returns dList
        // @description
        // Returns a list of regions that the location is in.
        // @Plugin WorldGuard
        // -->
        if (attribute.startsWith("regions")) {
            return new dList(getRegions()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {}

}
