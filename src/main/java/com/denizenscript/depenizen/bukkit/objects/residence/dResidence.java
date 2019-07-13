package com.denizenscript.depenizen.bukkit.objects.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class dResidence implements ObjectTag {

    public static dResidence valueOf(String string) {
        return dResidence.valueOf(string, null);
    }

    @Fetchable("residence")
    public static dResidence valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match residence name

        string = string.replace("residence@", "");
        ClaimedResidence residence = Residence.getInstance().getResidenceManagerAPI().getByName(string);
        if (residence == null) {
            return null;
        }
        return new dResidence(residence);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("residence@", "");
        return Residence.getInstance().getResidenceManagerAPI().getByName(arg) != null;
    }

    ClaimedResidence residence = null;

    public dResidence(ClaimedResidence residence) {
        if (residence != null) {
            this.residence = residence;
        }
        else {
            Debug.echoError("Residence referenced is null!");
        }
    }

    String prefix = "Residence";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Residence";
    }

    @Override
    public String identify() {
        return "residence@" + residence.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ClaimedResidence getResidence() {
        return residence;
    }

    public boolean equals(dResidence residence) {
        return CoreUtilities.toLowerCase(residence.getResidence().getName()).equals(CoreUtilities.toLowerCase(this.getResidence().getName()));
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <residence@residence.name>
        // @returns ElementTag
        // @description
        // Returns the name of the residence.
        // @Plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(residence.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <residence@residence.owner>
        // @returns PlayerTag
        // @description
        // Returns the owner of the residence.
        // @Plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("owner")) {
            return new PlayerTag(residence.getOwnerUUID()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <residence@residence.is_within[<location>]>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the specified location is within this Residence.
        // @Plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("is_within") && attribute.hasContext(1)) {
            LocationTag location = LocationTag.valueOf(attribute.getContext(1));
            return new ElementTag(residence.containsLoc(location)).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);
    }
}
