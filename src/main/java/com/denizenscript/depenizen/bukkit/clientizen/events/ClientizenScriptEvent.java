package com.denizenscript.depenizen.bukkit.clientizen.events;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.clientizen.DataDeserializer;
import com.denizenscript.depenizen.bukkit.clientizen.DataSerializer;
import org.bukkit.entity.Player;

public abstract class ClientizenScriptEvent extends ScriptEvent {

    public boolean enabled;
    public Player player;
    public String id;

    @Override
    public void init() {
        enabled = true;
    }

    @Override
    public void destroy() {
        enabled = false;
    }

    public void fireInternal(Player player, DataDeserializer data) {
        this.player = player;
        fire(data);
    }

    public void fire(DataDeserializer data) {
        fire();
    }

    // write isn't abstract to avoid events that don't need to send data having empty impls
    public void write(DataSerializer serializer) {}

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player);
    }
}
