package net.gnomeffinway.depenizen.extensions.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.residence.dResidence;

public class ResidencePlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static ResidencePlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ResidencePlayerExtension((dPlayer) object);
        }
    }

    private ResidencePlayerExtension(dPlayer player) {
        this.player = Residence.getPlayerManagerAPI().getResidencePlayer(player.getName());
    }

    ResidencePlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.has_main_residence>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player has a main Residence.
        // @Plugin Depenizen, Residence
        // -->
        if (attribute.startsWith("has_main_residence")) {
            ClaimedResidence residence = player.getMainResidence();
            return new Element(residence != null).getAttribute(attribute.fulfill(1));
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
        // @returns dList(dResidence)
        // @description
        // Returns the player's current list of Residences.
        // @Plugin Depenizen, Residence
        // -->
        else if (attribute.startsWith("residences")) {
            dList list = new dList();
            for (ClaimedResidence residence : player.getResList()) {
                list.add(new dResidence(residence).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
