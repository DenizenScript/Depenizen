package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

import java.util.HashMap;

public class BungeeServerDisconnectScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // bungee server disconnects
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when another server disconnects from the BungeeCord socket.
    //
    // @Switch server (BungeeServerTag) checks the server that disconnected.
    //
    // @Context
    // server (BungeeServerTag) returns the server that disconnected.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public BungeeServerDisconnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerDisconnectScriptEvent instance;
    public BungeeServerTag server;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee server disconnects");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return BungeeScriptEvent.checkServer(server.getInternal(), data, this::error);
    }

    @Override
    public String getName() {
        return "BungeeServerDisconnect";
    }

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> context = super.getDefinitions(data);
        context.put("server", server);
        return context;
    }
}
