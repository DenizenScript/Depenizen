package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class ResidenceLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ResidenceLocation";
    }

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static ResidenceLocationProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidenceLocationProperties((dLocation) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "has_residence", "residence"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private ResidenceLocationProperties(dLocation location) {
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
        // @Plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("has_residence")) {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
            return new Element(res != null).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <l@location.residence>
        // @returns dResidence
        // @description
        // Returns the residence contained by this location.
        // @Plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("residence")) {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
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
