package com.denizenscript.depenizen.bukkit.properties.breweryx;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.dre.brewery.BPlayer;

public class BreweryPlayerExtensions {

    public static void register() {
        // <--[tag]
        // @attribute <PlayerTag.brewery_drunkenness>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkenness of the brewery player or null if Brewery has no data on the player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "brewery_drunkenness", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getOfflinePlayer()) ? BPlayer.get(object.getOfflinePlayer()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getDrunkeness());
            }
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.brewery_quality>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the quality of the brewery player's drunkenness (drunkeness * drunkeness) or null if Brewery has no data on the player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "brewery_quality", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getOfflinePlayer()) ? BPlayer.get(object.getOfflinePlayer()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getQuality());
            }
            return null;
        });

        // <--[tag]
        // @attribute <PlayerTag.brewery_alcoholrecovery>
        // @returns ElementTag
        // @plugin Depenizen, BreweryX
        // @description
        // Returns the drunkenness reduction per minute or null if Brewery has no data on the player.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "brewery_alcoholrecovery", (attribute, object) -> {
            BPlayer bPlayer = BPlayer.hasPlayer(object.getOfflinePlayer()) ? BPlayer.get(object.getOfflinePlayer()) : null;
            if (bPlayer != null) {
                return new ElementTag(bPlayer.getAlcRecovery());
            }
            return null;
        });
    }
}
