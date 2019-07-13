package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.factions.dFaction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.util.IdUtil;
import com.denizenscript.denizen.objects.dNPC;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.debugging.dB;

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

    public static boolean describes(dObject object) {
        return object instanceof dPlayer || object instanceof dNPC;
    }

    public static FactionsPlayerNPCProperties getFrom(dObject object) {
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

    private FactionsPlayerNPCProperties(dObject object) {
        String name = object instanceof dPlayer ? ((dPlayer) object).getName()
                : object instanceof dNPC ? ((dNPC) object).getName()
                : null;
        if (name == null) {
            dB.echoError("Invalid dObject! Must be a dPlayer or dNPC!");
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
            // @attribute <p@player.factions.power>
            // @returns Element(Decimal)
            // @description
            // Returns the player's power level.
            // @Plugin Depenizen, Factions
            // -->
            // <--[tag]
            // @attribute <n@npc.factions.power>
            // @returns Element(Decimal)
            // @description
            // Returns the NPC's power level.
            // @Plugin Depenizen, Factions
            // -->
            if (attribute.startsWith("power")) {
                return new Element(player.getPower()).getAttribute(attribute.fulfill(1));
            }

            else if (player.hasFaction()) {

                // <--[tag]
                // @attribute <p@player.factions.role>
                // @returns Element
                // @description
                // Returns the player's role in their faction.
                // @Plugin Depenizen, Factions
                // -->
                // <--[tag]
                // @attribute <n@npc.factions.role>
                // @returns Element
                // @description
                // Returns the NPC's role in their faction.
                // @Plugin Depenizen, Factions
                // -->
                if (attribute.startsWith("role")) {
                    if (player.getRole() != null) {
                        return new Element(player.getRole().toString()).getAttribute(attribute.fulfill(1));
                    }
                }

                // <--[tag]
                // @attribute <p@player.factions.title>
                // @returns Element
                // @description
                // Returns the player's title.
                // @Plugin Depenizen, Factions
                // -->
                // <--[tag]
                // @attribute <n@npc.factions.title>
                // @returns Element
                // @description
                // Returns the NPC's title.
                // @Plugin Depenizen, Factions
                // -->
                else if (attribute.startsWith("title")) {
                    if (player.hasTitle()) {
                        return new Element(player.getTitle()).getAttribute(attribute.fulfill(1));
                    }
                }
            }

        }

        // <--[tag]
        // @attribute <p@player.faction>
        // @returns dFaction
        // @description
        // Returns the player's faction.
        // @Plugin Depenizen, Factions
        // -->
        // <--[tag]
        // @attribute <n@npc.faction>
        // @returns dFaction
        // @description
        // Returns the NPC's faction.
        // @Plugin Depenizen, Factions
        // -->
        else if (attribute.startsWith("faction")) {
            return new dFaction(player.getFaction()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
