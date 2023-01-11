package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;

public class ResidenceLocationExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.has_residence>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns whether the location has a Residence.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "has_residence", (attribute, location) -> {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
            return new ElementTag(res != null);
        });

        // <--[tag]
        // @attribute <LocationTag.residence>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the Residence contained by this location.
        // -->
        LocationTag.tagProcessor.registerTag(ResidenceTag.class, "residence", (attribute, location) -> {
            ClaimedResidence res = Residence.getInstance().getResidenceManager().getByLoc(location);
            if (res != null) {
                return new ResidenceTag(res);
            }
            return null;
        });
    }
}
