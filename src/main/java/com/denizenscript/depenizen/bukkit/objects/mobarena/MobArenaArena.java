package com.denizenscript.depenizen.bukkit.objects.mobarena;

import com.denizenscript.depenizen.bukkit.bridges.MobArenaBridge;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.entity.Player;

public class MobArenaArena implements ObjectTag {

    String prefix = "MobArena";
    Arena arena = null;

    public static MobArenaArena valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mobarena")
    public static MobArenaArena valueOf(String name, TagContext context) {
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
        return new MobArenaArena(arena);
    }

    public static boolean matches(String name) {
        return valueOf(name) != null;
    }

    public MobArenaArena(Arena arena) {
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
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
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
        // @attribute <mobarena@mobarena.name>
        // @returns ElementTag
        // @description
        // Returns the name of the arena.
        // @Plugin Depenizen, MobArena
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(arena.arenaName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.config_name>
        // @returns ElementTag
        // @description
        // Returns the configuration name of the arena.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("config_name")) {
            return new ElementTag(arena.configName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.is_running>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the arena is running.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("is_running")) {
            return new ElementTag(arena.isRunning()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.wave_count>
        // @returns ElementTag(Number)
        // @description
        // Returns the number of waves this arena has in total.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("wave_count")) {
            return new ElementTag(arena.getWaveManager().getFinalWave())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.current_wave>
        // @returns ElementTag(Number)
        // @description
        // Returns the current wave number.
        // NOTE: Requires the arena to be running.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("current_wave") && arena.isRunning()) {
            return new ElementTag(arena.getWaveManager().getWaveNumber())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.wave_type>
        // @returns ElementTag
        // @description
        // Returns the type of the current wave.
        // NOTE: Requires the arena to be running.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("wave_type") && arena.isRunning()) {
            return new ElementTag(arena.getWaveManager().getCurrent().getType().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.is_enabled>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the arena is enabled.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("is_enabled")) {
            return new ElementTag(arena.isEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.status>
        // @returns ElementTag
        // @description
        // Returns the status of the arena.
        // Will return 'closed', 'open', or 'running'.
        // @Plugin Depenizen, MobArena
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
            // @attribute <mobarena@mobarena.players.in_arena>
            // @returns ListTag(dPlayer)
            // @description
            // Returns a list of players in the arena.
            // @Plugin Depenizen, MobArena
            // -->
            if (attribute.getAttribute(2).startsWith("in_arena")) {
                ListTag players = new ListTag();
                for (Player p : arena.getPlayersInArena()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <mobarena@mobarena.players.in_lobby>
            // @returns ListTag(dPlayer)
            // @description
            // Returns a list of players in the lobby.
            // @Plugin Depenizen, MobArena
            // -->
            else if (attribute.getAttribute(2).startsWith("in_lobby")) {
                ListTag players = new ListTag();
                for (Player p : arena.getPlayersInLobby()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <mobarena@mobarena.players>
            // @returns ListTag(dPlayer)
            // @description
            // Returns a list of all players in the arena (including the lobby).
            // @Plugin Depenizen, MobArena
            // -->
            else {
                ListTag players = new ListTag();
                for (Player p : arena.getAllPlayers()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.type>
        // @returns ElementTag
        // @description
        // Always returns 'MobArena' for MobArena objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("type")) {
            return new ElementTag("MobArena").getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);
    }
}
