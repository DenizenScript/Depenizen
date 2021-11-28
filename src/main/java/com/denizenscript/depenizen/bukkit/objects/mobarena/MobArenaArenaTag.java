package com.denizenscript.depenizen.bukkit.objects.mobarena;

import com.denizenscript.depenizen.bukkit.bridges.MobArenaBridge;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.entity.Player;

public class MobArenaArenaTag implements ObjectTag {

    // <--[ObjectType]
    // @name MobArenaArenaTag
    // @prefix mobarena
    // @base ElementTag
    // @format
    // The identity format for arenas is <arena_name>
    // For example, 'mobarena@my_arena'.
    //
    // @plugin Depenizen, MobArena
    // @description
    // A MobArenaArenaTag represents a mob arena in the world.
    //
    // -->

    String prefix = "MobArena";
    Arena arena = null;

    public static MobArenaArenaTag valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mobarena")
    public static MobArenaArenaTag valueOf(String name, TagContext context) {
        if (name == null) {
            return null;
        }

        ////////
        // Match Arena name
        name = name.replace("mobarena@", "");
        Arena arena = ((MobArena) MobArenaBridge.instance.plugin).getArenaMaster().getArenaWithName(name);
        if (arena == null) {
            return null;
        }
        return new MobArenaArenaTag(arena);
    }

    public static boolean matches(String name) {
        return valueOf(name) != null;
    }

    public MobArenaArenaTag(Arena arena) {
        if (arena != null) {
            this.arena = arena;
        }
        else {
            Debug.echoError("Arena referenced is null");
        }
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "MobArena";
    }

    @Override
    public String identify() {
        return "mobarena@" + arena.configName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.name>
        // @returns ElementTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the name of the arena.
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(arena.arenaName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.config_name>
        // @returns ElementTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the configuration name of the arena.
        // -->
        else if (attribute.startsWith("config_name")) {
            return new ElementTag(arena.configName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.is_running>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MobArena
        // @description
        // Returns whether the arena is running.
        // -->
        else if (attribute.startsWith("is_running")) {
            return new ElementTag(arena.isRunning()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.wave_count>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MobArena
        // @description
        // Returns the number of waves this arena has in total.
        // -->
        else if (attribute.startsWith("wave_count")) {
            return new ElementTag(arena.getWaveManager().getFinalWave())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.current_wave>
        // @returns ElementTag(Number)
        // @plugin Depenizen, MobArena
        // @description
        // Returns the current wave number.
        // NOTE: Requires the arena to be running.
        // -->
        else if (attribute.startsWith("current_wave") && arena.isRunning()) {
            return new ElementTag(arena.getWaveManager().getWaveNumber())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.wave_type>
        // @returns ElementTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the type of the current wave.
        // NOTE: Requires the arena to be running.
        // -->
        else if (attribute.startsWith("wave_type") && arena.isRunning()) {
            return new ElementTag(arena.getWaveManager().getCurrent().getType().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.is_enabled>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, MobArena
        // @description
        // Returns whether the arena is enabled.
        // -->
        else if (attribute.startsWith("is_enabled")) {
            return new ElementTag(arena.isEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <MobArenaArenaTag.status>
        // @returns ElementTag
        // @plugin Depenizen, MobArena
        // @description
        // Returns the status of the arena.
        // Will return 'closed', 'open', or 'running'.
        // -->
        else if (attribute.startsWith("status")) {
            if (!arena.isEnabled()) {
                return new ElementTag("closed").getAttribute(attribute.fulfill(1));
            }
            else if (!arena.isRunning()) {
                return new ElementTag("open").getAttribute(attribute.fulfill(1));
            }
            else {
                return new ElementTag("running").getAttribute(attribute.fulfill(1));
            }
        }

        else if (attribute.startsWith("players")) {

            // <--[tag]
            // @attribute <MobArenaArenaTag.players.in_arena>
            // @returns ListTag(PlayerTag)
            // @plugin Depenizen, MobArena
            // @description
            // Returns a list of players in the arena.
            // -->
            if (attribute.getAttribute(2).startsWith("in_arena")) {
                ListTag players = new ListTag();
                for (Player p : arena.getPlayersInArena()) {
                    players.addObject(new PlayerTag(p));
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <MobArenaArenaTag.players.in_lobby>
            // @returns ListTag(PlayerTag)
            // @plugin Depenizen, MobArena
            // @description
            // Returns a list of players in the lobby.
            // -->
            else if (attribute.getAttribute(2).startsWith("in_lobby")) {
                ListTag players = new ListTag();
                for (Player p : arena.getPlayersInLobby()) {
                    players.addObject(new PlayerTag(p));
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <MobArenaArenaTag.players>
            // @returns ListTag(PlayerTag)
            // @plugin Depenizen, MobArena
            // @description
            // Returns a list of all players in the arena (including the lobby).
            // -->
            else {
                ListTag players = new ListTag();
                for (Player p : arena.getAllPlayers()) {
                    players.addObject(new PlayerTag(p));
                }
                return players.getAttribute(attribute.fulfill(1));
            }
        }

        return new ElementTag(identify()).getAttribute(attribute);
    }
}
