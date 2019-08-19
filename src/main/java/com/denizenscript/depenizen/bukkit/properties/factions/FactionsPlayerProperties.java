package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.objects.factions.FactionTag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.util.IdUtil;

public class FactionsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "FactionsPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static FactionsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "factions", "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private FactionsPlayerProperties(PlayerTag object) {
        player = object;
    }

    public MPlayer getMPlayer() {
        MPlayer mplayer = MPlayer.get(IdUtil.getId(player.getOfflinePlayer().getUniqueId()));
        return mplayer;
    }

    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("factions")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.factions.power>
            // @returns ElementTag(Decimal)
            // @description
            // Returns the player's power level.
            // @Plugin Depenizen, Factions
            // -->
            if (attribute.startsWith("power")) {
                return new ElementTag(getMPlayer().getPower()).getAttribute(attribute.fulfill(1));
            }

            else if (getMPlayer().hasFaction()) {

                // <--[tag]
                // @attribute <PlayerTag.factions.role>
                // @returns ElementTag
                // @description
                // Returns the player's role in their faction.
                // Note: In modern Factions these are called ranks instead of roles.
                // @Plugin Depenizen, Factions
                // -->
                if (attribute.startsWith("role")) {
                    if (getMPlayer().getRank() != null) {
                        return new ElementTag(getMPlayer().getRank().toString()).getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <PlayerTag.factions.title>
                // @returns ElementTag
                // @description
                // Returns the player's title.
                // @Plugin Depenizen, Factions
                // -->
                else if (attribute.startsWith("title")) {
                    if (getMPlayer().hasTitle()) {
                        return new ElementTag(getMPlayer().getTitle()).getAttribute(attribute.fulfill(1));
                    }
                }
            }

        }

        // <--[tag]
        // @attribute <PlayerTag.faction>
        // @returns FactionTag
        // @description
        // Returns the player's faction.
        // @Plugin Depenizen, Factions
        // -->
        else if (attribute.startsWith("faction")) {
            return new FactionTag(getMPlayer().getFaction()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
