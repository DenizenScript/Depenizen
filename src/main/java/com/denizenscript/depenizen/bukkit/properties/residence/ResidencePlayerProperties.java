package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;

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
        return object instanceof PlayerTag;
    }

    public static ResidencePlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidencePlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_main_residence", "main_residence", "residences"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private ResidencePlayerProperties(PlayerTag player) {
        this.player = Residence.getInstance().getPlayerManagerAPI().getResidencePlayer(player.getName());
    }

    ResidencePlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.has_main_residence>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns whether the player has a main Residence.
        // -->
        if (attribute.startsWith("has_main_residence")) {
            ClaimedResidence residence = player.getMainResidence();
            return new ElementTag(residence != null).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.main_residence>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the player's current main Residence if they have one.
        // -->
        else if (attribute.startsWith("main_residence")) {
            ClaimedResidence residence = player.getMainResidence();
            if (residence != null) {
                return new ResidenceTag(player.getMainResidence()).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <PlayerTag.residences>
        // @returns ListTag(ResidenceTag)
        // @plugin Depenizen, Residence
        // @description
        // Returns the player's current list of Residences.
        // -->
        else if (attribute.startsWith("residences")) {
            ListTag list = new ListTag();
            for (ClaimedResidence residence : player.getResList()) {
                list.addObject(new ResidenceTag(residence));
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
