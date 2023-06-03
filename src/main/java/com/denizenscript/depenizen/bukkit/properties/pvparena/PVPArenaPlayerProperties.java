package com.denizenscript.depenizen.bukkit.properties.pvparena;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArenaTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.arena.ArenaPlayer;

public class PVPArenaPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PVPArenaPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static PVPArenaPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PVPArenaPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "pvparena"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public PVPArenaPlayerProperties(PlayerTag player) {
        this.player = ArenaPlayer.parsePlayer(player.getName());
    }

    ArenaPlayer player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("pvparena")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.pvparena.in_arena[<pvparena>]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, PvP Arena
            // @description
            // Returns true if the player is in the specified arena. If no arena is specified,
            // this returns true if the player is in any arena.
            // -->
            if (attribute.startsWith("inarena") || attribute.startsWith("in_arena")) {
                if (player.getArena() == null) {
                    return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
                }
                if (attribute.hasParam()) {
                    PVPArenaArenaTag a = attribute.paramAsType(PVPArenaArenaTag.class);
                    if (a == null) {
                        return null;
                    }
                    return new ElementTag(CoreUtilities.toLowerCase(player.getArena().getName())
                            .equals(CoreUtilities.toLowerCase(a.getArena().getName())))
                            .getObjectAttribute(attribute.fulfill(1));
                }
                else {
                    return new ElementTag(player.getArena() != null)
                            .getObjectAttribute(attribute.fulfill(1));
                }
            }

            // <--[tag]
            // @attribute <PlayerTag.pvparena.class>
            // @returns ElementTag
            // @plugin Depenizen, PvP Arena
            // @description
            // Returns the player's class if they're in an arena. Otherwise, returns null.
            // -->
            else if (attribute.startsWith("class")) {
                return new ElementTag(player.getArenaClass().getName()).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.pvparena.is_ready>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, PvP Arena
            // @description
            // Returns true if the player is ready.
            // -->
            else if (attribute.startsWith("isready") || attribute.startsWith("is_ready")) {
                return new ElementTag(player.getStatus().equals(ArenaPlayer.Status.READY))
                        .getObjectAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("team")) {

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <PlayerTag.pvparena.team.player_count>
                // @returns ElementTag(Number)
                // @plugin Depenizen, PvP Arena
                // @description
                // Returns the number of players in the team.
                // -->
                if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
                    return new ElementTag(player.getArenaTeam().getTeamMembers().size())
                            .getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.pvparena.team.name>
                // @returns ElementTag
                // @plugin Depenizen, PvP Arena
                // @description
                // Returns the player's team name if they're in an arena. Otherwise, returns null.
                // -->
                if (attribute.startsWith("name")) {
                    return new ElementTag(player.getArenaTeam().getName())
                            .getObjectAttribute(attribute.fulfill(1));
                }
            }
        }
        return null;
    }
}
