package com.morphanone.depenizenbukkit.events.bungee;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import com.morphanone.depenizenbukkit.objects.bungee.dServer;

import java.util.HashMap;
import java.util.Map;

public class ServerSwitchScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // bungee player switches to server
    // bungee player switches to <server>
    //
    // @Triggers when a player switches to a different server on the BungeeCord network.
    //
    // @Cancellable false
    //
    // @Context
    // <context.uuid> returns an Element of the player's UUID.
    // <context.name> returns an Element of the player's current name.
    // <context.server> returns the dServer the player is switching to.
    //
    // @Plugin Depenizen, BungeeCord
    // -->


    public ServerSwitchScriptEvent() {
        instance = this;
    }

    public static ServerSwitchScriptEvent instance;
    public Element uuid;
    public Element name;
    public dServer server;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("bungee player switches to ");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return tryServer(server, CoreUtilities.getXthArg(4, s));
    }

    @Override
    public String getName() {
        return "ServerSwitch";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("uuid", uuid);
        context.put("name", name);
        context.put("server", server);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        uuid = new Element(context.get("uuid"));
        name = new Element(context.get("name"));
        server = dServer.getServerFromName(context.get("server"));
        fire();
        return null;
    }
}
