package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;

public class ResidencePlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ResidencePlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dPlayer;
    }

    public static ResidencePlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidencePlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_main_residence", "main_residence", "residences"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private ResidencePlayerProperties(dPlayer player) {
        this.player = Residence.getInstance().getPlayerManagerAPI().getResidencePlayer(player.getName());
    }

    ResidencePlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.has_main_residence>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the player has a main Residence.
        // @Plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("has_main_residence")) {
            ClaimedResidence residence = player.getMainResidence();
            return new ElementTag(residence != null).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.main_residence>
        // @returns dResidence
        // @description
        // Returns the player's current main Residence if they have one.
        // @Plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("main_residence")) {
            ClaimedResidence residence = player.getMainResidence();
            if (residence != null) {
                return new dResidence(player.getMainResidence()).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <p@player.residences>
        // @returns ListTag(dResidence)
        // @description
        // Returns the player's current list of Residences.
        // @Plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("residences")) {
            ListTag list = new ListTag();
            for (ClaimedResidence residence : player.getResList()) {
                list.add(new dResidence(residence).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
