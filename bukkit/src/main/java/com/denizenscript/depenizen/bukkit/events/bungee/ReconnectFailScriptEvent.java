package com.denizenscript.depenizen.bukkit.events.bungee;

import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class ReconnectFailScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee fails reconnect
    //
    // @Triggers when the socket client fails all of its reconnect attempts.
    //
    // @Cancellable false
    //
    // @Context
    // None
    //
    // @Plugin DepenizenBukkit, DepenizenBungee
    // -->

    public ReconnectFailScriptEvent() {
        instance = this;
    }

    public static ReconnectFailScriptEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee fails reconnect");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeReconnectFail";
    }
}
