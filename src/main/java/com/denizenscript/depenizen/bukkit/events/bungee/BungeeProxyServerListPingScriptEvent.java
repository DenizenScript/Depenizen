package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;

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
    // "MAX_PLAYERS:" + ElementTag(Number) to change the listed maximum number of players.
    // "VERSION:" + ElementTag to change the listed server version.
    // "MOTD:" + ElementTag to change the server MOTD that will be displayed.
    //
    // @Plugin Depenizen, BungeeCord
    //
    // -->

    public BungeeProxyServerListPingScriptEvent() {
        instance = this;
    }

    public static BungeeProxyServerListPingScriptEvent instance;

    public String address;

    public int currentPlayers;

    public int maxPlayers;

    public String motd;

    public int protocol;

    public String version;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!path.eventLower.startsWith("proxy server list ping")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptPath path) {
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
    public String getName() {
        return "BungeeProxyServerListPing";
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
                maxPlayers = ArgumentHelper.getIntegerFrom(determination.substring("max_players:".length()));
                return true;
            }
            else if (determinationLow.startsWith("version:")) {
                version = determination.substring("version:".length());
                return true;
            }
            else if (determinationLow.startsWith("motd:")) {
                motd = determination.substring("motd:".length());
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }


    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("address")) {
            return new ElementTag(address);
        }
        else if (name.equals("num_players") || name.equals("current_players")) {
            return new ElementTag(currentPlayers);
        }
        else if (name.equals("max_players")) {
            return new ElementTag(maxPlayers);
        }
        else if (name.equals("motd")) {
            return new ElementTag(motd);
        }
        else if (name.equals("protocol")) {
            return new ElementTag(protocol);
        }
        else if (name.equals("version")) {
            return new ElementTag(version);
        }
        return super.getContext(name);
    }
}
