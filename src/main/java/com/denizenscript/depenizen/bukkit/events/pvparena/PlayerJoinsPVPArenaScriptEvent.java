package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArenaTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.slipcor.pvparena.events.PAJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinsPVPArenaScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // pvparena player joins
    //
    // @Regex ^on pvparena player joins$
    //
    // @Cancellable true
    //
    // @Triggers when a player joins a pvparena.
    //
    // @Context
    // <context.arena> returns the arena denizen object.
    //
    // @Plugin Depenizen, PVPArena
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public static PlayerJoinsPVPArenaScriptEvent instance;
    public PAJoinEvent event;
    public PVPArenaArenaTag arena;

    public PlayerJoinsPVPArenaScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("pvparena player joins");
    }

    @Override
    public String getName() {
        return "PlayerJoinsPVPArena";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerJoinsPVPArena(PAJoinEvent event) {
        arena = new PVPArenaArenaTag(event.getArena());
        this.event = event;
        fire(event);
    }
}
