package com.denizenscript.depenizen.bukkit.properties.pvpstats;

import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import net.slipcor.pvpstats.PVPData;

public class PVPStatsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PVPStatsPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PVPStatsPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PVPStatsPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "pvpstats"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public PVPStatsPlayerProperties(dPlayer player) {
        this.playerName = player.getName();
    }

    String playerName = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("pvpstats")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.pvpstats.deaths>
            // @returns Element
            // @description
            // Returns the number of times the player has died.
            // @Plugin Depenizen, PvP Stats
            // -->
            if (attribute.startsWith("deaths")) {
                return new Element(PVPData.getDeaths(playerName)).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.pvpstats.elo>
            // @returns Element
            // @description
            // Returns the Elo rating of the player.
            // @Plugin Depenizen, PvP Stats
            // -->
            if (attribute.startsWith("elo")) {
                return new Element(PVPData.getEloScore(playerName)).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.pvpstats.kills>
            // @returns Element
            // @description
            // Returns the number of players the player has killed.
            // @Plugin Depenizen, PvP Stats
            // -->
            if (attribute.startsWith("kills")) {
                return new Element(PVPData.getKills(playerName)).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.pvpstats.streak>
            // @returns Element
            // @description
            // Returns the current kill streak of the player.
            // @Plugin Depenizen, PvP Stats
            // -->
            if (attribute.startsWith("streak")) {
                return new Element(PVPData.getStreak(playerName)).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.pvpstats.max_streak>
            // @returns Element
            // @description
            // Returns the highest kill streak of the player.
            // @Plugin Depenizen, PvP Stats
            // -->
            if (attribute.startsWith("max_streak")) {
                return new Element(PVPData.getMaxStreak(playerName)).getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }
}
