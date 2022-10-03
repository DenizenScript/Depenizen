package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.clientizen.network.DataDeserializer;
import org.bukkit.entity.Player;

import java.util.Map;

public class ClientizenEventScriptEvent extends ScriptEvent {

    public ClientizenEventScriptEvent() {
        instance = this;
        registerCouldMatcher("clientizen event");
        registerSwitches("id");
    }

    public static ClientizenEventScriptEvent instance;

    boolean enabled = false;

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
        switch (name) {
            case "id": return new ElementTag(id);
            case "data": return contextMap;
        }
        ObjectTag value = contextMap.getObject(name);
        if (value != null) {
            return value;
        }
        return super.getContext(name);
    }

    public void tryFire(Player source, DataDeserializer data) {
        if (!enabled) {
            return;
        }
        player = source;
        id = data.readString();
        contextMap = new MapTag();
        for (Map.Entry<String, String> entry : data.readStringMap().entrySet()) {
            contextMap.putObject(entry.getKey(), ObjectFetcher.pickObjectFor(entry.getValue(), CoreUtilities.noDebugContext));
        }
        fire();
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player);
    }

    @Override
    public void init() {
        enabled = true;
    }

    @Override
    public void destroy() {
        enabled = false;
    }
}
