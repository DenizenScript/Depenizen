package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

import java.util.HashMap;

public class BungeeServerConnectScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // bungee server connects
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when another server connects to the BungeeCord socket.
    //
    // @Switch server (BungeeServerTag) checks the server that connected.
    //
    // @Context
    // server (BungeeServerTag) returns the server that connected.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public BungeeServerConnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerConnectScriptEvent instance;
    public BungeeServerTag server;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee server connects");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return BungeeScriptEvent.checkServer(server.getInternal(), data, this::error);
    }

    @Override
    public String getName() {
        return "BungeeServerConnect";
    }

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> context = super.getDefinitions(data);
        context.put("server", server);
        return context;
    }
}
