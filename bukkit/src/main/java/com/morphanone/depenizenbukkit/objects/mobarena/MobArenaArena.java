package com.morphanone.depenizenbukkit.objects.mobarena;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import com.morphanone.depenizenbukkit.support.Support;
import com.morphanone.depenizenbukkit.support.plugins.MobArenaSupport;
import org.bukkit.entity.Player;

public class MobArenaArena implements dObject {

    String prefix = "MobArena";
    Arena arena = null;

    static MobArena plugin = Support.getPlugin(MobArenaSupport.class);

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
        Arena arena = plugin.getArenaMaster().getArenaWithName(name);
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
            dB.echoError("Arena referenced is null");
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
    public dObject setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.name>
        // @returns Element
        // @description
        // Returns the name of the arena.
        // @plugin Depenizen, MobArena
        // -->
        if (attribute.startsWith("name")) {
            return new Element(arena.arenaName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.config_name>
        // @returns Element
        // @description
        // Returns the configuration name of the arena.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("config_name")) {
            return new Element(arena.configName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.is_running>
        // @returns Element(Boolean)
        // @description
        // Returns whether the arena is running.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("is_running")) {
            return new Element(arena.isRunning()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.wave_count>
        // @returns Element(Number)
        // @description
        // Returns the number of waves this arena has in total.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("wave_count")) {
            return new Element(arena.getWaveManager().getFinalWave())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.current_wave>
        // @returns Element(Number)
        // @description
        // Returns the current wave number.
        // NOTE: Requires the arena to be running.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("current_wave") && arena.isRunning()) {
            return new Element(arena.getWaveManager().getWaveNumber())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.wave_type>
        // @returns Element
        // @description
        // Returns the type of the current wave.
        // NOTE: Requires the arena to be running.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("wave_type") && arena.isRunning()) {
            return new Element(arena.getWaveManager().getCurrent().getType().toString())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.is_enabled>
        // @returns Element(Boolean)
        // @description
        // Returns whether the arena is enabled.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("is_enabled")) {
            return new Element(arena.isEnabled()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.status>
        // @returns Element
        // @description
        // Returns the status of the arena.
        // Will return 'closed', 'open', or 'running'.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("status")) {
            if (!arena.isEnabled()) {
                return new Element("closed").getAttribute(attribute.fulfill(1));
            }
            else if (!arena.isRunning()) {
                return new Element("open").getAttribute(attribute.fulfill(1));
            }
            else {
                return new Element("running").getAttribute(attribute.fulfill(1));
            }
        }

        else if (attribute.startsWith("players")) {

            // <--[tag]
            // @attribute <mobarena@mobarena.players.in_arena>
            // @returns dList(dPlayer)
            // @description
            // Returns a list of players in the arena.
            // @plugin Depenizen, MobArena
            // -->
            if (attribute.getAttribute(2).startsWith("in_arena")) {
                dList players = new dList();
                for (Player p : arena.getPlayersInArena()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <mobarena@mobarena.players.in_lobby>
            // @returns dList(dPlayer)
            // @description
            // Returns a list of players in the lobby.
            // @plugin Depenizen, MobArena
            // -->
            else if (attribute.startsWith("in_lobby")) {
                dList players = new dList();
                for (Player p : arena.getPlayersInLobby()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(2));
            }

            // <--[tag]
            // @attribute <mobarena@mobarena.players>
            // @returns dList(dPlayer)
            // @description
            // Returns a list of all players in the arena (including the lobby).
            // @plugin Depenizen, MobArena
            // -->
            else {
                dList players = new dList();
                for (Player p : arena.getAllPlayers()) {
                    players.add(new dPlayer(p).identify());
                }
                return players.getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <mobarena@mobarena.type>
        // @returns Element
        // @description
        // Always returns 'MobArena' for MobArena objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, MobArena
        // -->
        else if (attribute.startsWith("type")) {
            return new Element("MobArena").getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
