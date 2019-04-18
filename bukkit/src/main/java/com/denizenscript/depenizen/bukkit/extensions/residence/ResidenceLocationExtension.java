package com.denizenscript.depenizen.bukkit.extensions.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class ResidenceLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static ResidenceLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidenceLocationExtension((dLocation) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    private ResidenceLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location;

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <l@location.has_residence>
        // @returns Element(Boolean)
        // @description
        // Returns if the location has a residence.
        // @Plugin DepenizenBukkit, Residence
        // -->
        if (attribute.startsWith("has_residence")) {
            org.bukkit.Location loc = new org.bukkit.Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(loc);
            return new Element(res != null).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <l@location.residence>
        // @returns dResidence
        // @description
        // Returns the residence contained by this location.
        // @Plugin DepenizenBukkit, Residence
        // -->
        if (attribute.startsWith("residence")) {
            org.bukkit.Location loc = new org.bukkit.Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(loc);
            if (res != null) {
                return new dResidence(res).getAttribute(attribute.fulfill(1));
            }
            return null;
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
