package com.denizenscript.depenizen.bukkit.events.bungee;

import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class BungeeRegisteredScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee registered
    //
    // @Regex ^on bungee registered$
    //
    // @Triggers when the socket client successfully registers with the BungeeCord socket.
    //
    // @Cancellable false
    //
    // @Context
    // None
    //
    // @Plugin DepenizenBukkit, DepenizenBungee
    // -->

    public BungeeRegisteredScriptEvent() {
        instance = this;
    }

    public static BungeeRegisteredScriptEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee registered");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeRegistered";
    }
}
