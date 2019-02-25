package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class BungeeServerDisconnectScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee server disconnects
    //
    // @Regex ^on bungee server disconnects$
    //
    // @Triggers when another server disconnects from the BungeeCord socket.
    //
    // @Cancellable false
    //
    // @Context
    // <context.server> returns the dServer that disconnected.
    //
    // @Plugin DepenizenBukkit, DepenizenBungee
    // -->

    public BungeeServerDisconnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerDisconnectScriptEvent instance;
    public dServer server;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee server disconnects");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeServerDisconnect";
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("server")) {
            return server;
        }
        return super.getContext(name);
    }
}
