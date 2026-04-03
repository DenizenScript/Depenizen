package com.denizenscript.depenizen.bukkit.properties.mobarena;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.bridges.MobArenaBridge;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArenaTag;
import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;

public class MobArenaPlayerExtensions {

    public static SlowWarning mobArenaPlayerTags = new SlowWarning("mobArenaPlayerTags", "Tags in the 'PlayerTag.mobarena.x' format have been deprecated: check the meta docs for more information.");

    public static Arena getCurrentArena(PlayerTag player) {
        return ((MobArena) MobArenaBridge.instance.plugin).getArenaMaster().getArenaWithPlayer(player.getPlayerEntity());
    }

    public static ArenaPlayer getArenaPlayer(PlayerTag player, Arena arena) {
        return arena.getArenaPlayer(player.getPlayerEntity());
    }

    public static void register() {

        // <--[tag]
        // @attribute <PlayerTag.in_mobarena>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MobArena
        // @description
        // Returns whether the player is in a mobarena.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "in_mobarena", (attribute, player) -> {
            return new ElementTag(getCurrentArena(player) != null);
        });

        // <--[tag]
        // @attribute <PlayerTag.current_mobarena>
        // @returns MobArenaArenaTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the arena the player is in.
        // NOTE: Requires the player to be in an arena.
        // -->
        PlayerTag.tagProcessor.registerTag(MobArenaArenaTag.class, "current_mobarena", (attribute, player) -> {
            Arena arena = getCurrentArena(player);
            return arena != null ? new MobArenaArenaTag(arena) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.mobarena_class>
        // @returns ElementTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the name of the class the player is using.
        // NOTE: Requires the player to be in an arena.
        // -->
        PlayerTag.tagProcessor.registerTag(ElementTag.class, "mobarena_class", (attribute, player) -> {
            Arena arena = getCurrentArena(player);
            return arena != null ? new ElementTag(getArenaPlayer(player, arena).getArenaClass().getConfigName(), true) : null;
        });

        // <--[tag]
        // @attribute <PlayerTag.mobarena_stats>
        // @returns MapTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the current stats of a player.
        // Includes keys 'KILLS', 'DAMAGE_DONE', 'DAMAGE_TAKEN', 'LAST_WAVE', 'TIMES_SWUNG', and 'TIMES_HIT' with ElementTag(Number) values.
        // NOTE: Requires the player to be in an arena.
        // -->
        PlayerTag.tagProcessor.registerTag(MapTag.class, "mobarena_stats", (attribute, player) -> {
            if (getCurrentArena(player) == null) {
                attribute.echoError("This player is not in an arena.");
                return null;
            }
            ArenaPlayerStatistics stats = getArenaPlayer(player, getCurrentArena(player)).getStats();
            MapTag values = new MapTag();
            values.putObject("kills", new ElementTag(stats.getInt("kills")));
            values.putObject("damage_done", new ElementTag(stats.getInt("dmgDone")));
            values.putObject("damage_taken", new ElementTag(stats.getInt("dmgTaken")));
            values.putObject("last_wave", new ElementTag(stats.getInt("lastWave")));
            values.putObject("times_swung", new ElementTag(stats.getInt("swings")));
            values.putObject("times_hit", new ElementTag(stats.getInt("hits")));
            return values;
        });

        PlayerTag.tagProcessor.registerTag(ObjectTag.class, "mobarena", (attribute, player) -> {
            mobArenaPlayerTags.warn(attribute.context);

            // <--[tag]
            // @attribute <PlayerTag.mobarena.in_arena>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, MobArena
            // @deprecated use 'PlayerTag.in_mobarena'
            // @description
            // Deprecated in favor of <@link tag PlayerTag.in_mobarena>.
            // -->
            if (attribute.startsWith("in_arena", 2)) {
                attribute.fulfill(1);
                return new ElementTag(getCurrentArena(player) != null);
            }
            Arena arena = getCurrentArena(player);
            if (arena != null) {

                // <--[tag]
                // @attribute <PlayerTag.mobarena.current_arena>
                // @returns MobArenaArenaTag
                // @plugin Depenizen, MobArena
                // @deprecated use 'PlayerTag.current_mobarena'
                // @description
                // Deprecated in favor of <@link tag PlayerTag.current_mobarena>.
                // -->
                if (attribute.startsWith("current_arena", 2)) {
                    attribute.fulfill(1);
                    return new MobArenaArenaTag(arena);
                }

                // <--[tag]
                // @attribute <PlayerTag.mobarena.class>
                // @returns ElementTag
                // @plugin Depenizen, MobArena
                // @deprecated use 'PlayerTag.mobarena_class'
                // @description
                // Deprecated in favor of <@link tag PlayerTag.mobarena_class>.
                // -->
                else if (attribute.startsWith("class", 2)) {
                    attribute.fulfill(1);
                    return new ElementTag(getArenaPlayer(player, arena).getArenaClass().getConfigName(), true);
                }

                else if (attribute.startsWith("stats", 2)) {
                    attribute.fulfill(1);
                    ArenaPlayerStatistics stats = getArenaPlayer(player, arena).getStats();
                    if (stats == null) {
                        return null;
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].kills>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[kills]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'KILLS' key.
                    // -->
                    if (attribute.startsWith("kills", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("kills"));
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].damage_done>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[damage_done]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'DAMAGE_DONE' key.
                    // @description
                    // Returns the amount of damage the player has dealt in the arena.
                    // -->
                    else if (attribute.startsWith("damage_done", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("dmgDone"));
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].damage_taken>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[damage_taken]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'DAMAGE_TAKEN' key.
                    // -->
                    else if (attribute.startsWith("damage_taken", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("dmgTaken"));
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].last_wave>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[last_wave]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'LAST_WAVE' key.
                    // -->
                    else if (attribute.startsWith("last_wave", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("lastWave"));
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].times_swung>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[times_swung]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'TIMES_SWUNG' key.
                    // -->
                    else if (attribute.startsWith("times_swung", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("swings"));
                    }

                    // <--[tag]
                    // @attribute <PlayerTag.mobarena.stats[<mobarena>].times_hit>
                    // @returns ElementTag(Number)
                    // @plugin Depenizen, MobArena
                    // @deprecated use 'PlayerTag.mobarena_stats.get[times_hit]'
                    // @description
                    // Deprecated in favor of <@link tag PlayerTag.mobarena_stats> with the 'TIMES_HIT' key.
                    // -->
                    else if (attribute.startsWith("times_hit", 2)) {
                        attribute.fulfill(1);
                        return new ElementTag(stats.getInt("hits"));
                    }
                }
            }
            return null;
        });
    }
}
