package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;

import java.util.HashMap;
import java.util.Map;

public class ProxyPingScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // proxy server list ping
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when the BungeeCord proxy is pinged from a client's server list.
    //
    // @Context
    // address (TextTag) returns the IP address of the request.
    // num_players (NumberTag) returns the number of players currently on the server.
    // max_players (NumberTag) returns an TextTag(Number) of the maximum players allowed on the server.
    // motd (TextTag) returns the server's MOTD.
    // protocol (NumberTag) returns the protocol number being used.
    // version (TextTag) returns the version of the proxy.
    //
    // @Determinations
    // num_players (NumberTag) to change the response for the number of online players.
    // max_players (NumberTag) to change the response for the number of maximum players.
    // version (TextTag) to change the response for the server version.
    // motd (TextTag) to change response for the MOTD.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public ProxyPingScriptEvent() {
        instance = this;
    }

    public static ProxyPingScriptEvent instance;
    public TextTag address;
    public NumberTag numPlayers;
    public NumberTag maxPlayers;
    public TextTag motd;
    public NumberTag protocol;
    public TextTag version;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("proxy server list ping");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    @Override
    public String getName() {
        return "ProxyPing";
    }

    @Override
    public void applyDetermination(boolean errors, String determination, AbstractTagObject value) {
        if (determination.equals("num_players")) {
            numPlayers = NumberTag.getFor(this::error, value);
        }
        else if (determination.startsWith("max_players")) {
            maxPlayers = NumberTag.getFor(this::error, value);
        }
        else if (determination.equals("version")) {
            version = TextTag.getFor(this::error, value);
        }
        else if (determination.equals("motd")) {
            motd = TextTag.getFor(this::error, value);
        }
        else {
            super.applyDetermination(errors, determination, value);
        }
    }

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> context = super.getDefinitions(data);
        context.put("address", address);
        context.put("motd", motd);
        context.put("num_players", numPlayers);
        context.put("max_players", maxPlayers);
        context.put("protocol", protocol);
        context.put("version", version);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        address = new TextTag(context.get("address"));
        numPlayers = NumberTag.getFor(this::error, context.get("num_players"));
        motd = new TextTag(context.get("motd"));
        maxPlayers = NumberTag.getFor(this::error, ("max_players"));
        protocol = NumberTag.getFor(this::error, ("protocol"));
        version = new TextTag(context.get("version"));
        run();
        Map<String, String> determinations = new HashMap<>();
        determinations.put("num_players", numPlayers.toString());
        determinations.put("max_players", maxPlayers.toString());
        determinations.put("motd", motd.toString());
        determinations.put("version", version.toString());
        return determinations;
    }
}
