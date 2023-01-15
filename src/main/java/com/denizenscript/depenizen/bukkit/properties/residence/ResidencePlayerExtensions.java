package com.denizenscript.depenizen.bukkit.properties.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;

public class ResidencePlayerExtensions {

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.has_main_residence>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Residence
        // @description
        // Returns whether the player has a main Residence.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "has_main_residence", (attribute, player) -> {
            ClaimedResidence res = Residence.getInstance().getPlayerManager().getResidencePlayer(player.getUUID()).getMainResidence();
            return new ElementTag(res != null);
        });

        // <--[tag]
        // @attribute <PlayerTag.main_residence>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the player's current main Residence if they have one.
        // -->
        PlayerTag.tagProcessor.registerTag(ResidenceTag.class, "main_residence", (attribute, player) -> {
            ClaimedResidence res = Residence.getInstance().getPlayerManager().getResidencePlayer(player.getUUID()).getMainResidence();
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
        PlayerTag.tagProcessor.registerTag(ListTag.class, "residences", (attribute, player) -> {
            ListTag list = new ListTag();
            for (ClaimedResidence res : Residence.getInstance().getPlayerManager().getResidencePlayer(player.getUUID()).getResList()) {
                list.addObject(new ResidenceTag(res));
            }
            return list;
        });
    }
}
