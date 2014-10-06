package net.gnomeffinway.depenizen.support.plugins;

import com.massivecraft.factions.entity.*;
import com.massivecraft.massivecore.ps.PS;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.objects.dFaction;
import net.gnomeffinway.depenizen.support.Support;

import java.util.ArrayList;

public class FactionsSupport extends Support {

    public FactionsSupport() {
        registerObjects(dFaction.class);
        registerAdditionalTags("factions", "faction");
    }

    @Override
    public String playerTags(dPlayer player, Attribute attribute) {
        return handlePlayer(player.getName(), attribute);
    }

    @Override
    public String npcTags(dNPC npc, Attribute attribute) {
        return handlePlayer(npc.getName(), attribute);
    }

    @Override
    public String locationTags(dLocation location, Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.faction>
        // @returns dFaction
        // @description
        // Returns the faction at the location. (Can also be SafeZone, WarZone, or Wilderness)
        // @plugin Factions
        // -->
        if (attribute.startsWith("faction"))
            return new dFaction(BoardColls.get().getFactionAt(PS.valueOf(location)))
                    .getAttribute(attribute.fulfill(1));

        return null;

    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("factions")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <factions.list_factions>
            // @returns dList(dFaction)
            // @description
            // Returns a list of all current factions.
            // @plugin Factions
            // -->
            if (attribute.startsWith("list_factions")) {
                ArrayList<dFaction> factions = new ArrayList<dFaction>();

                for (FactionColl fc : FactionColls.get().getColls())
                    for (Faction f : fc.getAll())
                        factions.add(new dFaction(f));

                return new dList(factions).getAttribute(attribute.fulfill(1));
            }

        }

        else if (attribute.startsWith("faction")) {

            for (FactionColl fc : FactionColls.get().getColls()) {
                for (Faction f : fc.getAll()) {
                    if (f.getId().equalsIgnoreCase(attribute.getContext(1))
                            || f.getName().equalsIgnoreCase(attribute.getContext(1))) {
                        return new dFaction(f).getAttribute(attribute.fulfill(1));
                    }
                }
            }

        }

        return null;

    }

    public String handlePlayer(String name, Attribute attribute) {

        UPlayer player = null;
        for (UPlayerColl upc : UPlayerColls.get().getColls()) {
            for (UPlayer up : upc.getAll())
                if (up.getName().equalsIgnoreCase(name))
                    player = up;
        }
        if (player == null) return null;

        if (attribute.startsWith("factions")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.factions.power>
            // @returns Element(Double)
            // @description
            // Returns the player's power level.
            // @plugin Factions
            // -->
            // <--[tag]
            // @attribute <n@npc.factions.power>
            // @returns Element(Double)
            // @description
            // Returns the NPC's power level.
            // @plugin Factions
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
                // @plugin Factions
                // -->
                // <--[tag]
                // @attribute <n@npc.factions.role>
                // @returns Element
                // @description
                // Returns the NPC's role in their faction.
                // @plugin Factions
                // -->
                if (attribute.startsWith("role")) {
                    if (player.getRole() != null)
                        return new Element(player.getRole().toString()).getAttribute(attribute.fulfill(1));
                    else
                        return Element.NULL.getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.factions.title>
                // @returns Element
                // @description
                // Returns the player's title.
                // @plugin Factions
                // -->
                // <--[tag]
                // @attribute <n@npc.factions.title>
                // @returns Element
                // @description
                // Returns the NPC's title.
                // @plugin Factions
                // -->
                else if (attribute.startsWith("title")) {
                    if (player.hasTitle())
                        return new Element(player.getTitle()).getAttribute(attribute.fulfill(1));
                    else
                        return Element.NULL.getAttribute(attribute.fulfill(1));
                }
            }

        }

        // <--[tag]
        // @attribute <p@player.faction>
        // @returns dFaction
        // @description
        // Returns the player's faction.
        // @plugin Factions
        // -->
        // <--[tag]
        // @attribute <n@npc.faction>
        // @returns dFaction
        // @description
        // Returns the NPC's faction.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("faction")) {
            return new dFaction(player.getFaction()).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
