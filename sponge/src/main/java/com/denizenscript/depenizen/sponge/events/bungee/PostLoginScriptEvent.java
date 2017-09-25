package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;

import java.util.HashMap;
import java.util.Map;

public class PostLoginScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // bungee player joins network
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when a player is connected to the BungeeCord network and is ready to join a server.
    //
    // @Context
    // uuid (TextTag) returns the player's UUID.
    // name (TextTag) returns the player's current name.
    //
    // @Determinations
    // None.
    //
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // -->

    public PostLoginScriptEvent() {
        instance = this;
    }

    public static PostLoginScriptEvent instance;
    public TextTag uuid;
    public TextTag name;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee player joins network");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    @Override
    public String getName() {
        return "PostLogin";
    }

    @Override
    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> context = super.getDefinitions(data);
        context.put("uuid", uuid);
        context.put("name", name);
        return context;
    }

    @Override
    public Map<String, String> fire(Map<String, String> context) {
        uuid = new TextTag(context.get("uuid"));
        name = new TextTag(context.get("name"));
        run();
        return null;
    }
}
