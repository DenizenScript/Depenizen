package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
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

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.has_main_residence>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns boolean whether the player has a main Residence.
        // -->
        PropertyParser.registerTag(ResidencePlayerProperties.class, ElementTag.class, "has_main_residence", (attribute, object) -> {
            ClaimedResidence res = object.player.getMainResidence();
            return new ElementTag(res != null);
        });

        // <--[tag]
        // @attribute <PlayerTag.main_residence>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the player's current main Residence if they have one.
        // -->
        PropertyParser.registerTag(ResidencePlayerProperties.class, ResidenceTag.class, "main_residence", (attribute, object) -> {
            ClaimedResidence res = object.player.getMainResidence();
            if (res != null) {
                return new ResidenceTag(res);
            }
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.residences>
        // @returns ListTag(ResidenceTag)
        // @plugin Depenizen, Residence
        // @description
        // Returns the player's current list of Residences.
        // -->
        PropertyParser.registerTag(ResidencePlayerProperties.class, ListTag.class, "residences", (attribute, object) -> {
            ListTag list = new ListTag();
            for (ClaimedResidence res : object.player.getResList()) {
                list.addObject(new ResidenceTag(res));
            }
            return list;
        });
    }
}
