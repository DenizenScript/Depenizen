package com.denizenscript.depenizen.bukkit.objects.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class ResidenceTag implements ObjectTag {

    public static ObjectTagProcessor<ResidenceTag> tagProcessor = new ObjectTagProcessor<>();

    // <--[ObjectType]
    // @name ResidenceTag
    // @prefix residence
    // @base ElementTag
    // @ExampleTagBase residence[myresidence]
    // @ExampleValues myresidence
    // @format
    // The identity format for residences is just the name.
    // For example, 'residence@myresidence'.
    //
    // @plugin Depenizen, Residence
    // @description
    // A ResidenceTag represents a Residence in the world.
    //
    // @Matchable
    // ResidenceTag matchers, sometimes identified as "<residence>":
    // "residence" plaintext: always matches.
    // Residence name: matches if the residence is named matches the input, using advanced matchers.
    // -->

    @Fetchable("residence")
    public static ResidenceTag valueOf(String string, TagContext context) {
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
        return new ResidenceTag(residence);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("residence@", "");
        return Residence.getInstance().getResidenceManagerAPI().getByName(arg) != null;
    }

    ClaimedResidence residence = null;

    public ResidenceTag(ClaimedResidence residence) {
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
    public boolean isUnique() {
        return true;
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

    public boolean equals(ResidenceTag residence) {
        return CoreUtilities.equalsIgnoreCase(residence.getResidence().getName(), this.getResidence().getName());
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    public static void register() {

        // <--[tag]
        // @attribute <ResidenceTag.name>
        // @returns ElementTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the name of this Residence.
        // -->
        tagProcessor.registerTag(ElementTag.class, "name", (attribute, object) -> {
            return new ElementTag(object.getResidence().getName());
        });

        // <--[tag]
        // @attribute <ResidenceTag.owner>
        // @returns PlayerTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the owner of the Residence.
        // -->
        tagProcessor.registerTag(PlayerTag.class, "owner", (attribute, object) -> {
            return new PlayerTag(object.getResidence().getOwnerUUID());
        });

        // <--[tag]
        // @attribute <ResidenceTag.subzones>
        // @returns ListTag(ResidenceTag)
        // @plugin Depenizen, Residence
        // @description
        // Returns a list of subzones in this Residence.
        // -->
        tagProcessor.registerTag(ListTag.class, "subzones", (attribute, object) -> {
            ListTag list = new ListTag();
            for (ClaimedResidence subzone : object.getResidence().getSubzones()) {
                list.addObject(new ResidenceTag(subzone));
            }
            return list;
        });

        // <--[tag]
        // @attribute <ResidenceTag.area>
        // @returns CuboidTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the area of the Residences.
        // -->
        tagProcessor.registerTag(CuboidTag.class, "area", (attribute, object) -> {
            CuboidArea area = object.getResidence().getMainArea();
            return new CuboidTag(area.getLowLocation(), area.getHighLocation());
        });

        // <--[tag]
        // @attribute <ResidenceTag.is_within[<location>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns whether the specified location is within this Residence.
        // -->
        tagProcessor.registerTag(ElementTag.class, LocationTag.class, "is_within", (attribute, object, loc) -> {
            return new ElementTag(object.getResidence().containsLoc(loc));
        });
    }

    @Override
    public boolean advancedMatches(String matcher) {
        String matcherLow = CoreUtilities.toLowerCase(matcher);
        if (matcherLow.equals("residence")) {
            return true;
        }
        return ScriptEvent.runGenericCheck(matcherLow, getResidence().getName());
    }
}
