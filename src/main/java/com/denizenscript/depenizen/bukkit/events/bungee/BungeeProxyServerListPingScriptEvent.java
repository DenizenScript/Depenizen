package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    // "ALTERNATE_PLAYER_TEXT:<ListTag>" to set custom text for the player list section of the server status. (Requires "Allow restricted actions" in Denizen/config.yml). Usage of this to present lines that look like player names (but aren't) is forbidden.
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

    public static class PlayerInfo {
        public UUID id;

        public String name;
    }

    public static class PingData {

        public String address;

        public int currentPlayers;

        public int maxPlayers;

        public String motd;

        public int protocol;

        public String version;

        public List<PlayerInfo> playerSample;
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
                List<PlayerTag> players = ListTag.valueOf(determination.substring("players:".length()), getTagContext(path)).filter(PlayerTag.class, getTagContext(path));
                data.playerSample = new ArrayList<>(players.size());
                for (PlayerTag player : players) {
                    PlayerInfo info = new PlayerInfo();
                    info.id = player.getUUID();
                    info.name = player.getName();
                    data.playerSample.add(info);
                }
                return true;
            }
            else if (determinationLow.startsWith("alternate_player_text:")) {
                if (!CoreConfiguration.allowRestrictedActions) {
                    Debug.echoError("Cannot use 'alternate_player_text' in proxy list ping event: 'Allow restricted actions' is disabled in Denizen config.yml.");
                    return true;
                }
                List<String> text = ListTag.valueOf(determination.substring("alternate_player_text:".length()), getTagContext(path));
                data.playerSample = new ArrayList<>(text.size());
                for (String line : text) {
                    PlayerInfo info = new PlayerInfo();
                    info.name = line;
                    info.id = new UUID(0, 0);
                    data.playerSample.add(info);
                }
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "address" -> new ElementTag(data.address);
            case "num_players", "current_players" -> new ElementTag(data.currentPlayers);
            case "max_players" -> new ElementTag(data.maxPlayers);
            case "motd" -> new ElementTag(data.motd);
            case "protocol" -> new ElementTag(data.protocol);
            case "version" -> new ElementTag(data.version);
            default -> super.getContext(name);
        };
    }
}
