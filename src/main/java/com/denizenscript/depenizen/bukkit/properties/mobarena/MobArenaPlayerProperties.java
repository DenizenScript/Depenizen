package com.denizenscript.depenizen.bukkit.properties.mobarena;

import com.denizenscript.depenizen.bukkit.bridges.MobArenaBridge;
import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class MobArenaPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "MobArenaPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dPlayer;
    }

    public static MobArenaPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MobArenaPlayerProperties((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "mobarena"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public MobArenaPlayerProperties(dPlayer player) {
        this.player = player;
        this.arena = ((MobArena) MobArenaBridge.instance.plugin).getArenaMaster().getArenaWithPlayer(player.getPlayerEntity());
    }

    dPlayer player = null;
    Arena arena = null;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("mobarena")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.mobarena.in_arena>
            // @returns ElementTag(Boolean)
            // @description
            // Returns whether the player is in an arena.
            // @Plugin Depenizen, MobArena
            // -->
            if (attribute.startsWith("in_arena")) {
                return new ElementTag(arena != null)
                        .getAttribute(attribute.fulfill(1));
            }

            else if (arena != null) {

                // <--[tag]
                // @attribute <p@player.mobarena.current_arena>
                // @returns mobarena
                // @description
                // Returns the arena the player is in.
                // NOTE: requires the player to be in an arena.
                // @Plugin Depenizen, MobArena
                // -->
                if (attribute.startsWith("current_arena")) {
                    return new MobArenaArena(arena).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.class>
                // @returns ElementTag
                // @description
                // Returns the name of the class the player is using.
                // NOTE: requires the player to be in an arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("class")) {
                    return new ElementTag(new ArenaPlayer(player.getPlayerEntity(), arena,
                            ((MobArena) MobArenaBridge.instance.plugin)).getStats().getClassName())
                            .getAttribute(attribute.fulfill(1));
                }

                return null;
            }

            else if (attribute.startsWith("stats") && attribute.hasContext(1)) {
                MobArenaArena a = MobArenaArena.valueOf(attribute.getContext(1));
                if (a == null) {
                    return null;
                }
                ArenaPlayerStatistics stats = new ArenaPlayer(player.getPlayerEntity(),
                        a.getArena(), ((MobArena) MobArenaBridge.instance.plugin)).getStats();

                attribute = attribute.fulfill(1);
                if (stats == null) {
                    return null;
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].kills>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of kills the player has in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                if (attribute.startsWith("kills")) {
                    return new ElementTag(stats.getInt("kills")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].damage_done>
                // @returns ElementTag(Number)
                // @description
                // Returns the amount of damage the player has dealt in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("damage_done")) {
                    return new ElementTag(stats.getInt("dmgDone")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].damage_taken>
                // @returns ElementTag(Number)
                // @description
                // Returns the amount of damage the player has taken in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("damage_taken")) {
                    return new ElementTag(stats.getInt("dmgTaken")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].last_wave>
                // @returns ElementTag(Number)
                // @description
                // Returns the wave the player reached in their last match in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("last_wave")) {
                    return new ElementTag(stats.getInt("lastWave")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].times_swung>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of times the player has swung their weapon in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("times_swung")) {
                    return new ElementTag(stats.getInt("swings")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].times_hit>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of times the player has hit an enemy in the arena.
                // @Plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("times_hit")) {
                    return new ElementTag(stats.getInt("hits")).getAttribute(attribute.fulfill(1));
                }

                return null;
            }
        }

        return null;
    }
}
