package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;

public class ResidenceLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ResidenceLocation";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static ResidenceLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidenceLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_residence", "residence"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private ResidenceLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.has_residence>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns boolean whether the location has a Residence.
        // -->
        PropertyParser.registerTag(ResidenceLocationProperties.class, ElementTag.class, "has_residence", (attribute, object) -> {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(object.location);
            return new ElementTag(res != null);
        });

        // <--[tag]
        // @attribute <LocationTag.residence>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the Residence contained by this location.
        // -->
        PropertyParser.registerTag(ResidenceLocationProperties.class, ResidenceTag.class, "residence", (attribute, object) -> {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(object.location);
            if (res != null) {
                return new ResidenceTag(res);
            }
            return null;
        });
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag value = mechanism.getValue();
    }
}
