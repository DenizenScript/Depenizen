package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;

import java.util.HashMap;
import java.util.Map;

public class PlayerDisconnectScriptEvent extends BungeeScriptEvent {

    // <--[event]
    // @Events
    // bungee player leaves network
    //
    // @Updated 2017/09/25
    //
    // @Group BungeeCord
    //
    // @Cancellable false
    //
    // @Triggers when a player leaves the BungeeCord network.
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

    public PlayerDisconnectScriptEvent() {
        instance = this;
    }

    public static PlayerDisconnectScriptEvent instance;
    public TextTag uuid;
    public TextTag name;

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("bungee player leaves network");
    }

    @Override
    public boolean matches(ScriptEventData scriptEventData) {
        return true;
    }

    @Override
    public String getName() {
        return "PlayerDisconnect";
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
