package com.denizenscript.depenizen.bukkit.objects.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class dResidence implements dObject {

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
        ClaimedResidence residence = Residence.getResidenceManagerAPI().getByName(string);
        if (residence == null) {
            return null;
        }
        return new dResidence(residence);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("residence@", "");
        return Residence.getResidenceManagerAPI().getByName(arg) != null;
    }

    ClaimedResidence residence = null;

    public dResidence(ClaimedResidence residence) {
        if (residence != null) {
            this.residence = residence;
        }
        else {
            dB.echoError("Residence referenced is null!");
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
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <residence@residence.name>
        // @returns Element
        // @description
        // Returns the name of the residence.
        // @plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("name")) {
            return new Element(residence.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <residence@residence.owner>
        // @returns dPlayer
        // @description
        // Returns the owner of the residence.
        // @plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("owner")) {
            return new dPlayer(residence.getOwnerUUID()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <residence@residence.is_within[<location>]>
        // @returns Element(Boolean)
        // @description
        // Returns whether the specified location is within this Residence.
        // @plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("is_within") && attribute.hasContext(1)) {
            dLocation location = dLocation.valueOf(attribute.getContext(1));
            return new Element(residence.containsLoc(location)).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
