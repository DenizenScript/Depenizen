package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;

import java.util.List;

public class BungeeProxyServerListPingScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // proxy server list ping
    //
    // @Regex ^on proxy server list ping$
    //
    // @Triggers when the bungeecord server is pinged and will return server details.
    //
    // @Context
    // <context.address> returns the source address of the ping.
    // <context.current_players> returns the number of players listed as currently online.
    // <context.max_players> return the maximum number of players that can join according to the list.
    // <context.motd> returns the server MOTD that will be displayed.
    // <context.protocol> returns the protocol number that will be given to the ping requester.
    // <context.version> returns the name of the server version that will be given to the ping requester.
    //
    // @Determine
    // "MAX_PLAYERS:<ElementTag(Number)>" to change the listed maximum number of players.
    // "VERSION:<ElementTag>" to change the listed server version.
    // "MOTD:<ElementTag>" to change the server MOTD that will be displayed.
    // "PLAYERS:<List(PlayerTag)>" to set what players are displayed in the "online players sample" view of the list ping.
    //
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    //
    // @Group Depenizen
    //
    // -->

    public BungeeProxyServerListPingScriptEvent() {
        instance = this;
    }

    public static BungeeProxyServerListPingScriptEvent instance;

    public static class PingData {

        public String address;

        public int currentPlayers;

        public int maxPlayers;

        public String motd;

        public int protocol;

        public String version;

        public List<PlayerTag> playerSample;
    }

    public PingData data;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!path.eventLower.startsWith("proxy server list ping")) {
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        BungeeBridge.instance.controlsProxyPing = true;
        BungeeBridge.instance.checkBroadcastProxyPing();
    }
    @Override
    public void destroy() {
        BungeeBridge.instance.controlsProxyPing = false;
        BungeeBridge.instance.checkBroadcastProxyPing();
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String determinationLow = CoreUtilities.toLowerCase(determination);
            if (determinationLow.startsWith("max_players:")) {
                data.maxPlayers = Integer.parseInt(determination.substring("max_players:".length()));
                return true;
            }
            else if (determinationLow.startsWith("version:")) {
                data.version = determination.substring("version:".length());
                return true;
            }
            else if (determinationLow.startsWith("motd:")) {
                data.motd = determination.substring("motd:".length());
                return true;
            }
            else if (determinationLow.startsWith("players:")) {
                data.playerSample = ListTag.valueOf(determination.substring("players:".length()), getTagContext(path)).filter(PlayerTag.class, getTagContext(path));
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "address":
                return new ElementTag(data.address);
            case "num_players":
            case "current_players":
                return new ElementTag(data.currentPlayers);
            case "max_players":
                return new ElementTag(data.maxPlayers);
            case "motd":
                return new ElementTag(data.motd);
            case "protocol":
                return new ElementTag(data.protocol);
            case "version":
                return new ElementTag(data.version);
        }
        return super.getContext(name);
    }
}
