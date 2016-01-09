package net.gnomeffinway.depenizen.extensions.mobarena;


import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.mobarena.MobArenaArena;
import net.gnomeffinway.depenizen.support.plugins.MobArenaSupport;

public class MobArenaPlayerExtension extends dObjectExtension {

    MobArena plugin = MobArenaSupport.getPlugin();

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static MobArenaPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new MobArenaPlayerExtension((dPlayer) object);
        }
    }

    public MobArenaPlayerExtension(dPlayer player) {
        this.player = player;
        this.arena = plugin.getArenaMaster().getArenaWithPlayer(player.getPlayerEntity());
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
            // @returns Element(Boolean)
            // @description
            // Returns whether the player is in an arena.
            // @plugin Depenizen, MobArena
            // -->
            if (attribute.startsWith("in_arena")) {
                return new Element(arena != null)
                        .getAttribute(attribute.fulfill(1));
            }

            else if (arena != null) {

                // <--[tag]
                // @attribute <p@player.mobarena.current_arena>
                // @returns mobarena
                // @description
                // Returns the arena the player is in.
                // NOTE: requires the player to be in an arena.
                // @plugin Depenizen, MobArena
                // -->
                if (attribute.startsWith("current_arena")) {
                    return new MobArenaArena(arena).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.class>
                // @returns Element
                // @description
                // Returns the name of the class the player is using.
                // NOTE: requires the player to be in an arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("class")) {
                    return new Element(new ArenaPlayer(player.getPlayerEntity(), arena, plugin).getStats().getClassName())
                            .getAttribute(attribute.fulfill(1));
                }

                return null;
            }

            else if (attribute.startsWith("stats") && attribute.hasContext(1)) {
                MobArenaArena a = MobArenaArena.valueOf(attribute.getContext(1));
                if (a == null) {
                    return null;
                }
                ArenaPlayerStatistics stats = new ArenaPlayer(player.getPlayerEntity(), a.getArena(), plugin).getStats();

                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].kills>
                // @returns Element(Number)
                // @description
                // Returns the number of kills the player has in the arena.
                // @plugin Depenizen, MobArena
                // -->
                if (attribute.startsWith("kills")) {
                    return new Element(stats.getInt("kills")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].damage_done>
                // @returns Element(Number)
                // @description
                // Returns the amount of damage the player has dealt in the arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("damage_done")) {
                    return new Element(stats.getInt("dmgDone")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].damage_taken>
                // @returns Element(Number)
                // @description
                // Returns the amount of damage the player has taken in the arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("damage_taken")) {
                    return new Element(stats.getInt("dmgTaken")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].last_wave>
                // @returns Element(Number)
                // @description
                // Returns the wave the player reached in their last match in the arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("last_wave")) {
                    return new Element(stats.getInt("lastWave")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].times_swung>
                // @returns Element(Number)
                // @description
                // Returns the number of times the player has swung their weapon in the arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("times_swung")) {
                    return new Element(stats.getInt("swings")).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.mobarena.stats[<mobarena>].times_hit>
                // @returns Element(Number)
                // @description
                // Returns the number of times the player has hit an enemy in the arena.
                // @plugin Depenizen, MobArena
                // -->
                else if (attribute.startsWith("times_hit")) {
                    return new Element(stats.getInt("hits")).getAttribute(attribute.fulfill(1));
                }

                return null;
            }
        }

        return null;
    }
}
