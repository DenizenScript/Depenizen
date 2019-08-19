package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.factions.FactionTag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.denizenscript.denizen.objects.NPCTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class FactionsPlayerNPCProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "FactionsPlayerNPC";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    // TODO: Refactor this into two separate extension classes you psychotic idiots.

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag || object instanceof NPCTag;
    }

    public static FactionsPlayerNPCProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsPlayerNPCProperties(object);
        }
    }

    public static final String[] handledTags = new String[] {
            "factions", "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private FactionsPlayerNPCProperties(ObjectTag object) {
        String name = object instanceof PlayerTag ? ((PlayerTag) object).getName()
                : object instanceof NPCTag ? ((NPCTag) object).getName()
                : null;
        if (name == null) {
            Debug.echoError("Invalid ObjectTag! Must be a PlayerTag or NPCTag!");
            return;
        }
        player = MPlayer.get(IdUtil.getId(name));
        if (player == null) {
            // Attempt to force NPCs into Factions
            player = MPlayerColl.get().create(IdUtil.getId(name));
        }
    }

    MPlayer player = null;

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
            // <--[tag]
            // @attribute <NPCTag.factions.power>
            // @returns ElementTag(Decimal)
            // @description
            // Returns the NPC's power level.
            // @Plugin Depenizen, Factions
            // -->
            if (attribute.startsWith("power")) {
                return new ElementTag(player.getPower()).getAttribute(attribute.fulfill(1));
            }

            else if (player.hasFaction()) {

                // <--[tag]
                // @attribute <PlayerTag.factions.role>
                // @returns ElementTag
                // @description
                // Returns the player's role in their faction.
                // Note: In modern Factions these are called ranks instead of roles.
                // @Plugin Depenizen, Factions
                // -->
                // <--[tag]
                // @attribute <NPCTag.factions.role>
                // @returns ElementTag
                // @description
                // Returns the NPC's role in their faction.
                // Note: In modern Factions these are called ranks instead of roles.
                // @Plugin Depenizen, Factions
                // -->
                if (attribute.startsWith("role")) {
                    if (player.getRank() != null) {
                        return new ElementTag(player.getRank().toString()).getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <PlayerTag.factions.title>
                // @returns ElementTag
                // @description
                // Returns the player's title.
                // @Plugin Depenizen, Factions
                // -->
                // <--[tag]
                // @attribute <NPCTag.factions.title>
                // @returns ElementTag
                // @description
                // Returns the NPC's title.
                // @Plugin Depenizen, Factions
                // -->
                else if (attribute.startsWith("title")) {
                    if (player.hasTitle()) {
                        return new ElementTag(player.getTitle()).getAttribute(attribute.fulfill(1));
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
        // <--[tag]
        // @attribute <NPCTag.faction>
        // @returns FactionTag
        // @description
        // Returns the NPC's faction.
        // @Plugin Depenizen, Factions
        // -->
        else if (attribute.startsWith("faction")) {
            return new FactionTag(player.getFaction()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
