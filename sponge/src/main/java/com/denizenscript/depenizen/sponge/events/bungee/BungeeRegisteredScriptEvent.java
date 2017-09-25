package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.events.ScriptEvent;

public class BungeeRegisteredScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // bungee registered
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when the socket client successfully registers with the BungeeCord socket.
    //
    // @Context
    // None.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public BungeeRegisteredScriptEvent() {
        instance = this;
    }

    public static BungeeRegisteredScriptEvent instance;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee registered");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeRegistered";
    }
}
