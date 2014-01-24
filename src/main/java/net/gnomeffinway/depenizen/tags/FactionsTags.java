package net.gnomeffinway.depenizen.tags;

import com.massivecraft.factions.entity.*;
import com.massivecraft.mcore.ps.PS;
import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dFaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class FactionsTags implements Listener {

    public FactionsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void factionsTags(ReplaceableTagEvent event) {

        // Build a new attribute out of the raw_tag supplied in the script to be fulfilled
        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());

        /////////////////////
        //   PLAYER TAGS
        /////////////////

        if (event.matches("player, pl")) {

            // PlayerTags require a... dPlayer!
            dPlayer p = event.getPlayer();

            // Player tag may specify a new player in the <player[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid player and update the dPlayer object reference.
                if (dPlayer.matches(attribute.getContext(1)))
                    p = dPlayer.valueOf(attribute.getContext(1));
                else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid player!");
                    return;
                }

            if (p == null || !p.isValid()) {
                dB.echoDebug("Invalid or missing player for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            UPlayer player = null;
            for (UPlayerColl upc : UPlayerColls.get().getColls()) {
                for (UPlayer up : upc.getAll())
                    if (up.getName().equalsIgnoreCase(p.getName()))
                        player = up;
            }
            if (player == null) return;

            attribute = attribute.fulfill(1);

            if (attribute.startsWith("factions")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.factions.power>
                // @returns Element(Double)
                // @description
                // Returns the player's power level.
                // @plugin Factions
                // -->
                if (attribute.startsWith("power")) {
                    event.setReplaced(new Element(player.getPower()).getAttribute(attribute.fulfill(1)));
                    return;
                }

                else if (player.hasFaction()) {

                    // <--[tag]
                    // @attribute <p@player.factions.role>
                    // @returns Element
                    // @description
                    // Returns the player's role in their faction.
                    // @plugin Factions
                    // -->
                    if (attribute.startsWith("role")) {
                        if (player.getRole() != null)
                            event.setReplaced(new Element(player.getRole().toString()).getAttribute(attribute.fulfill(1)));
                        else
                            event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                        return;
                    }

                    // <--[tag]
                    // @attribute <p@player.factions.title>
                    // @returns Element
                    // @description
                    // Returns the player's title.
                    // @plugin Factions
                    // -->
                    else if (attribute.startsWith("title")) {
                        if (player.hasTitle())
                            event.setReplaced(new Element(player.getTitle()).getAttribute(attribute.fulfill(1)));
                        else
                            event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                        return;
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
            else if (attribute.startsWith("faction")) {
                event.setReplaced(new dFaction(player.getFaction()).getAttribute(attribute.fulfill(1)));
            }
        }

        /////////////////////
        //   NPC TAGS
        /////////////////

        else if (event.matches("npc")) {

            dNPC n = event.getNPC();

            if (attribute.hasContext(1))
                if (dNPC.matches(attribute.getContext(1)))
                    n = dNPC.valueOf(attribute.getContext(1));
                else {
                    dB.echoDebug(event.getScriptEntry(), "Could not match '" + attribute.getContext(1) + "' to a valid NPC!");
                    return;
                }

            if (n == null || !n.isValid()) {
                dB.echoDebug(event.getScriptEntry(), "Invalid or missing NPC for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            UPlayer npc = null;
            for (UPlayerColl upc : UPlayerColls.get().getColls()) {
                for (UPlayer up : upc.getAll())
                    if (up.getName().equalsIgnoreCase(n.getName()))
                        npc = up;
            }
            if (npc == null) return;

            attribute = attribute.fulfill(1);

            if (attribute.startsWith("factions")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <n@npc.factions.power>
                // @returns Element(Double)
                // @description
                // Returns the NPC's power level.
                // @plugin Factions
                // -->
                if (attribute.startsWith("power")) {
                    event.setReplaced(new Element(npc.getPower()).getAttribute(attribute.fulfill(1)));
                    return;
                }

                else if (npc.hasFaction()) {

                    // <--[tag]
                    // @attribute <n@npc.factions.role>
                    // @returns Element
                    // @description
                    // Returns the NPC's role in their faction.
                    // @plugin Factions
                    // -->
                    if (attribute.startsWith("role")) {
                        if (npc.getRole() != null)
                            event.setReplaced(new Element(npc.getRole().toString()).getAttribute(attribute.fulfill(1)));
                        else
                            event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                        return;
                    }

                    // <--[tag]
                    // @attribute <n@npc.factions.title>
                    // @returns Element
                    // @description
                    // Returns the NPC's title.
                    // @plugin Factions
                    // -->
                    else if (attribute.startsWith("title")) {
                        if (npc.hasTitle())
                            event.setReplaced(new Element(npc.getTitle()).getAttribute(attribute.fulfill(1)));
                        else
                            event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                        return;
                    }
                }

            }

            // <--[tag]
            // @attribute <n@npc.faction>
            // @returns dFaction
            // @description
            // Returns the NPC's faction.
            // @plugin Factions
            // -->
            else if (attribute.startsWith("faction")) {
                event.setReplaced(new dFaction(npc.getFaction()).getAttribute(attribute.fulfill(1)));
            }

        }

        /////////////////////
        //   LOCATION TAGS
        /////////////////

        else if (event.matches("location, l")) {

            dLocation loc = null;

            // Check name context for a specified location, or check
            // the ScriptEntry for a 'location' context
            if (event.hasNameContext() && dLocation.matches(event.getNameContext()))
                loc = dLocation.valueOf(event.getNameContext());
            else if (event.getScriptEntry().hasObject("location"))
                loc = (dLocation) event.getScriptEntry().getObject("location");

            // Check if location is null, return null if it is
            if (loc == null) {
                event.setReplaced("null");
                return;
            }

            attribute.fulfill(1);

            // <--[tag]
            // @attribute <l@location.faction>
            // @returns dFaction
            // @description
            // Returns the faction at the location. (Can also be SafeZone, WarZone, or Wilderness)
            // @plugin Factions
            // -->
            if (attribute.startsWith("faction"))
                event.setReplaced(new dFaction(BoardColls.get().getFactionAt(PS.valueOf(loc)))
                        .getAttribute(attribute.fulfill(1)));

        }

        /////////////////////
        //   FACTION TAGS
        /////////////////

        else if (event.matches("factions")) {

            attribute = event.getAttributes().fulfill(1);

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

                event.setReplaced(new dList(factions).getAttribute(attribute.fulfill(1)));
            }

        }

        else if (event.matches("faction")) {

            for (FactionColl fc : FactionColls.get().getColls()) {
                for (Faction f : fc.getAll()) {
                    if (f.getId().equalsIgnoreCase(attribute.getContext(1))
                            || f.getName().equalsIgnoreCase(attribute.getContext(1))) {
                        event.setReplaced(new dFaction(f).getAttribute(attribute.fulfill(1)));
                    }
                }
            }

        }

    }

}
