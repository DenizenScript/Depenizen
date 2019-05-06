package com.denizenscript.depenizen.bukkit.extensions.pvparena;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.arena.ArenaPlayer;

public class PVPArenaPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PVPArenaPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PVPArenaPlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "pvparena"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PVPArenaPlayerExtension(dPlayer player) {
        this.player = ArenaPlayer.parsePlayer(player.getName());
    }

    ArenaPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("pvparena")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.pvparena.in_arena[<pvparena>]>
            // @returns Element(Boolean)
            // @description
            // Returns true if the player is in the specified arena. If no arena is specified,
            // this returns true if the player is in any arena.
            // @Plugin DepenizenBukkit, PvP Arena
            // -->
            if (attribute.startsWith("inarena") || attribute.startsWith("in_arena")) {
                if (player.getArena() == null) {
                    return new Element(false).getAttribute(attribute.fulfill(1));
                }
                if (attribute.hasContext(1)) {
                    PVPArenaArena a = PVPArenaArena.valueOf(attribute.getContext(1));
                    if (a == null) {
                        return null;
                    }
                    return new Element(CoreUtilities.toLowerCase(player.getArena().getName())
                            .equals(CoreUtilities.toLowerCase(a.getArena().getName())))
                            .getAttribute(attribute.fulfill(1));
                }
                else {
                    return new Element(player.getArena() != null)
                            .getAttribute(attribute.fulfill(1));
                }
            }

            // <--[tag]
            // @attribute <p@player.pvparena.class>
            // @returns Element
            // @description
            // Returns the player's class if they're in an arena. Otherwise, returns null.
            // @Plugin DepenizenBukkit, PvP Arena
            // -->
            else if (attribute.startsWith("class")) {
                return new Element(player.getArenaClass().getName()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.pvparena.is_ready>
            // @returns Element(Boolean)
            // @description
            // Returns true if the player is ready.
            // @Plugin DepenizenBukkit, PvP Arena
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
                // @Plugin DepenizenBukkit, PvP Arena
                // -->
                if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                    return new Element(player.getArenaTeam().getTeamMembers().size())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.pvparena.team.name>
                // @returns Element
                // @description
                // Returns the player's team name if they're in an arena. Otherwise, returns null.
                // @Plugin DepenizenBukkit, PvP Arena
                // -->
                if (attribute.startsWith("name")) {
                    return new Element(player.getArenaTeam().getName())
                            .getAttribute(attribute.fulfill(1));
                }
            }
        }
        return null;
    }
}
