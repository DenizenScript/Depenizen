package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.events.ScriptEvent;

public class ReconnectFailScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // bungee fails reconnect
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when the socket client fails all of its reconnect attempts.
    //
    // @Context
    // None.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public ReconnectFailScriptEvent() {
        instance = this;
    }

    public static ReconnectFailScriptEvent instance;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee fails reconnect");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeReconnectFail";
    }
}
