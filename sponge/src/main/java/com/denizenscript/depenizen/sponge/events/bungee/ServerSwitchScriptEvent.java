package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

import java.util.HashMap;
import java.util.Map;

public class ServerSwitchScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // bungee player switches to server
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when a player switches to a different server on the BungeeCord network.
    //
    // @Switch server (BungeeServerTag) checks the server the player is switching to.
    //
    // @Context
    // uuid (TextTag) returns the player's UUID.
    // name (TextTag) returns the player's current name.
    // server (BungeeServerTag) returns the server the player is switching to.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public ServerSwitchScriptEvent() {
        instance = this;
    }

    public static ServerSwitchScriptEvent instance;
    public TextTag uuid;
    public TextTag name;
    public BungeeServerTag server;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee player switches to server");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return BungeeScriptEvent.checkServer(server.getInternal(), data, this::error);
    }

    @Override
    public String getName() {
        return "ServerSwitch";
    }

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> context = super.getDefinitions(data);
        context.put("uuid", uuid);
        context.put("name", name);
        context.put("server", server);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        uuid = new TextTag(context.get("uuid"));
        name = new TextTag(context.get("name"));
        server = BungeeServerTag.getFor(this::error, context.get("server"));
        run();
        return null;
    }
}
