package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

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
    // "MAX_PLAYERS:" + Element(Number) to change the listed maximum number of players.
    // "VERSION:" + Element to change the listed server version.
    // "MOTD:" + Element to change the server MOTD that will be displayed.
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
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return s.startsWith("proxy server list ping");
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
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String determinationLow = CoreUtilities.toLowerCase(determination);
        if (determinationLow.startsWith("max_players:")) {
            maxPlayers = aH.getIntegerFrom(determination.substring("max_players:".length()));
        }
        else if (determinationLow.startsWith("version:")) {
            version = determination.substring("version:".length());
        }
        else if (determinationLow.startsWith("motd:")) {
            motd = determination.substring("motd:".length());
        }
        return super.applyDetermination(container, determination);
    }


    @Override
    public dObject getContext(String name) {
        if (name.equals("address")) {
            return new Element(address);
        }
        else if (name.equals("num_players") || name.equals("current_players")) {
            return new Element(currentPlayers);
        }
        else if (name.equals("max_players")) {
            return new Element(maxPlayers);
        }
        else if (name.equals("motd")) {
            return new Element(motd);
        }
        else if (name.equals("protocol")) {
            return new Element(protocol);
        }
        else if (name.equals("version")) {
            return new Element(version);
        }
        return super.getContext(name);
    }
}
