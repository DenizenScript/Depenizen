package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.events.PVPArenaEvents;
import net.gnomeffinway.depenizen.support.Support;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.managers.ArenaManager;

public class PVPArenaSupport extends Support {

    public PVPArenaSupport() {
        registerEvents(PVPArenaEvents.class);
        registerAdditionalTags("pvparena");
    }

    @Override
    public String playerTags(dPlayer p, Attribute attribute) {

        ArenaPlayer player = ArenaPlayer.parsePlayer(p.getName());

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
                return new Element(player.getArenaClass().getName()).getAttribute(attribute.fulfill(1));
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
                    return new Element(player.getArena().getName().equalsIgnoreCase(attribute.getContext(1)))
                            .getAttribute(attribute.fulfill(1));
                } else {
                    return new Element(player.getStatus().equals(ArenaPlayer.Status.FIGHT))
                            .getAttribute(attribute.fulfill(1));
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
                return new Element(player.getStatus().equals(ArenaPlayer.Status.READY))
                        .getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("team")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.pvparena.team.player_count>
                // @returns Element(Integer)
                // @description
                // Returns the number of players in the team.
                // @plugin PvP Arena
                // -->
                if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                    return new Element(player.getArenaTeam().getTeamMembers().size())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.pvparena.team>
                // @returns Element
                // @description
                // Returns the player's team name if they're in an arena. Otherwise, returns null.
                // @plugin PvP Arena
                // -->
                return new Element(player.getArenaTeam().getName())
                        .getAttribute(attribute.fulfill(1));

            }

        }

        return null;

    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("pvparena")) {

            // PvPArena tags require a... PvPArena!
            Arena a = null;

            // PvPArena tag may specify a new PvPArena in the <pvparena[context]...> portion of the tag.
            if (attribute.hasContext(1))
                // Check if this is a valid PvPArena
                if (ArenaManager.getArenaByName(attribute.getContext(1)) != null) {
                    a = ArenaManager.getArenaByName(attribute.getContext(1));
                } else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid arena!");
                    return null;
                }

            if (a == null) {
                dB.echoError("Invalid or missing arena!");
                return null;
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
                return new Element(a.getFighters().size()).getAttribute(attribute.fulfill(1));
            }

        }

        return null;

    }

}
