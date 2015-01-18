package net.gnomeffinway.depenizen.extensions.pvparena;

import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.slipcor.pvparena.arena.ArenaPlayer;

public class PVPArenaPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static PVPArenaPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new PVPArenaPlayerExtension((dPlayer) pl);
    }

    private PVPArenaPlayerExtension(dPlayer pl) {
        player = ArenaPlayer.parsePlayer(pl.getName());
    }

    ArenaPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("pvparena")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.pvparena.class>
            // @returns Element
            // @description
            // Returns the player's class if they're in an arena. Otherwise, returns null.
            // @plugin Depenizen, PvP Arena
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
            // @plugin Depenizen, PvP Arena
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
            // @plugin Depenizen, PvP Arena
            // -->
            else if (attribute.startsWith("isready") || attribute.startsWith("is_ready")) {
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
                // @plugin Depenizen, PvP Arena
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
                // @plugin Depenizen, PvP Arena
                // -->
                return new Element(player.getArenaTeam().getName())
                        .getAttribute(attribute.fulfill(1));

            }

        }

        return null;

    }

}
