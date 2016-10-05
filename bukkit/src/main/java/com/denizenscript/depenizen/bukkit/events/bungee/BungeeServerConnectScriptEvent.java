package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class BungeeServerConnectScriptEvent extends BukkitScriptEvent {

    // <--[event]
    // @Events
    // bungee server connects
    //
    // @Triggers when another server connects to the BungeeCord socket.
    //
    // @Cancellable false
    //
    // @Context
    // <context.server> returns the dServer that connected.
    //
    // @Plugin DepenizenBukkit, DepenizenBungee
    // -->

    public BungeeServerConnectScriptEvent() {
        instance = this;
    }

    public static BungeeServerConnectScriptEvent instance;
    public dServer server;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee server connects");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "BungeeServerConnect";
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("server")) {
            return server;
        }
        return super.getContext(name);
    }
}
