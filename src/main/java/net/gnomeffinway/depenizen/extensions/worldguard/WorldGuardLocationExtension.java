package net.gnomeffinway.depenizen.extensions.worldguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dList;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.worldguard.WorldGuardRegion;
import net.gnomeffinway.depenizen.support.Supported;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGuardLocationExtension extends dObjectExtension {

    public static boolean describes(dObject loc) {
        return loc instanceof dLocation;
    }

    public static WorldGuardLocationExtension getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new WorldGuardLocationExtension((dLocation) loc);
    }

    private WorldGuardLocationExtension(dLocation loc) {
        location = loc;
    }

    dLocation location = null;

    private ApplicableRegionSet getApplicableRegions() {
        WorldGuardPlugin worldGuard = Supported.get("WORLDGUARD").getPlugin();
        return worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location);
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
        // @attribute <l@location.in_region[<name>|...]>
        // @returns Element(Boolean)
        // @description
        // If a region name or list of names is specified, returns whether the
        // location is in one of the listed regions, otherwise returns whether
        // the location is in any region.
        // @Plugin Depenizen, WorldGuard
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
        // @returns dList(Region)
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
