package net.gnomeffinway.depenizen.tags;

import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.arena.ArenaPlayer.Status;
import net.slipcor.pvparena.managers.ArenaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PVPArenaTags implements Listener {

    public PVPArenaTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void pvpArenaTags(ReplaceableTagEvent event) {

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
                if (dPlayer.matches(attribute.getContext(1))) {
                    p = dPlayer.valueOf(attribute.getContext(1));
                } else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid player!");
                    return;
                }

            if (p == null || !p.isValid()) {
                dB.echoDebug("Invalid or missing player for tag <" + event.raw_tag + ">!");
                event.setReplaced("null");
                return;
            }

            ArenaPlayer player = ArenaPlayer.parsePlayer(p.getName());
            attribute = attribute.fulfill(1);

            if (attribute.startsWith("pvparena")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.pvparena.class>
                // @returns Element
                // @description
                // Returns the player's class if they're in an arena. Otherwise, returns null.
                // @plugin PvP Arena
                // -->
                if (attribute.startsWith("class")) {
                    event.setReplaced(new Element(player.getArenaClass().toString())
                            .getAttribute(attribute.fulfill(1)));
                }

                // <--[tag]
                // @attribute <p@player.pvparena.in_arena[<arena>]>
                // @returns Element(Boolean)
                // @description
                // Returns true if the player is in the specified arena. If no arena is specified,
                // this returns true if the player is in any arena.
                // @plugin PvP Arena
                // -->
                else if (attribute.startsWith("inarena") || attribute.startsWith("in_arena")) {
                    if (attribute.hasContext(1)) {
                        event.setReplaced(new Element(player.getArena().getName().equalsIgnoreCase(attribute.getContext(1)))
                                .getAttribute(attribute.fulfill(1)));
                    } else {
                        event.setReplaced(new Element(player.getStatus().equals(Status.FIGHT))
                                .getAttribute(attribute.fulfill(1)));
                    }
                }

                // <--[tag]
                // @attribute <p@player.pvparena.is_ready>
                // @returns Element(Boolean)
                // @description
                // Returns true if the player is ready.
                // @plugin PvP Arena
                // -->
                else if (attribute.startsWith("isempty") || attribute.startsWith("is_ready")) {
                    event.setReplaced(new Element(player.getStatus().equals(Status.READY))
                            .getAttribute(attribute.fulfill(1)));
                } else if (attribute.startsWith("team")) {

                    attribute = attribute.fulfill(1);

                    // <--[tag]
                    // @attribute <p@player.pvparena.team.player_count>
                    // @returns Element(Integer)
                    // @description
                    // Returns the number of players in the team.
                    // @plugin PvP Arena
                    // -->
                    if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                        event.setReplaced(new Element(player.getArenaTeam().getTeamMembers().size())
                                .getAttribute(attribute.fulfill(1)));
                        return;
                    }

                    // <--[tag]
                    // @attribute <p@player.pvparena.team>
                    // @returns Element
                    // @description
                    // Returns the player's team name if they're in an arena. Otherwise, returns null.
                    // @plugin PvP Arena
                    // -->
                    event.setReplaced(new Element(player.getArenaTeam().getName())
                            .getAttribute(attribute.fulfill(1)));
                }

            }

        } else if (event.matches("pvparena")) {

            // PvPArena tags require a... PvPArena!
            Arena a = null;

            // PvPArena tag may specify a new PvPArena in the <pvparena[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid PvPArena
                if (ArenaManager.getArenaByName(attribute.getContext(1)) != null) {
                    a = ArenaManager.getArenaByName(attribute.getContext(1));
                } else {
                    dB.echoDebug("Could not match '"
                            + attribute.getContext(1) + "' to a valid job!");
                    return;
                }

            if (a == null) {
                dB.echoDebug("Invalid or missing arena for tag <" + event.raw_tag + ">!");
                event.setReplaced(new Element("null").getAttribute(attribute.fulfill(1)));
                return;
            }

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <pvparena[<arena>].player_count>
            // @returns Element(Integer)
            // @description
            // Returns the number of players in the arena.
            // @plugin PvP Arena
            // -->
            if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                event.setReplaced(new Element(a.getFighters().size())
                        .getAttribute(attribute.fulfill(1)));
            }

        }
    }

}
