package com.denizenscript.depenizen.bukkit.properties.breweryx;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.dre.brewery.BPlayer;

public class BreweryPlayerExtensions {

    public static void register() {
        // <--[tag]
        // @attribute <BPlayerTag.drunkenness>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkness of the brewery player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "drunkenness", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getPlayerEntity()) ? BPlayer.get(object.getPlayerEntity()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getDrunkeness());
            }
            return null;
        });

        // <--[tag]
        // @attribute <BPlayerTag.quality>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the quality of the brewery player's drunkenness (drunkeness * drunkeness).
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "quality", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getPlayerEntity()) ? BPlayer.get(object.getPlayerEntity()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getQuality());
            }
            return null;
        });

        // <--[tag]
        // @attribute <BPlayerTag.alcrecovery>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkenness reduction per minute.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "alcrecovery", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getPlayerEntity()) ? BPlayer.get(object.getPlayerEntity()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getAlcRecovery());
            }
            return null;
        });
    }
}
