package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.entity.Player;

import java.util.Map;

public class ClientizenEventScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // clientizen event
    //
    // @Group Clientizen
    //
    // @Warning The client can send any data it wants, so should very carefully verify any input before using it.
    //
    // @Switch id:<id> to only process the event if the identifier received from the client matches the specified matcher.
    //
    // @Triggers When the server receives an event from a clientizen client, this requires config option 'Clientizen.process events' in the Depenizen config.
    //
    // @Context
    // <context.id> returns an ElementTag of the event identifier received from the client.
    // <context.data> returns the context MapTag received from the client as a map of keys to ElementTags.
    // <context.(key)> returns the ElementTag value of the input key in the context map, if available.
    //
    // @Player Always.
    //
    // @Example
    // # Use to listen to Clientizen events with the id 'my_id' and send the player that sent them a message.
    // on clientizen event id:my_id:
    // - narrate "Hello! you sent an event with the id '<context.id>'"
    //
    // -->

    public static ClientizenEventScriptEvent instance;

    public ClientizenEventScriptEvent() {
        instance = this;
        registerCouldMatcher("clientizen event");
        registerSwitches("id");
    }

    public MapTag contextMap;
    public String id;
    public Player player;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "id", id)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "id" -> new ElementTag(id);
            case "data" -> contextMap;
            default -> {
                ElementTag value = contextMap.getElement(name);
                yield value != null ? value : super.getContext(name);
            }
        };
    }

    public void fire(Player source, String id, Map<String, String> contexts) {
        player = source;
        this.id = id;
        contextMap = new MapTag();
        for (Map.Entry<String, String> entry : contexts.entrySet()) {
            contextMap.putObject(entry.getKey(), new ElementTag(entry.getValue()));
        }
        fire();
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player);
    }
}
