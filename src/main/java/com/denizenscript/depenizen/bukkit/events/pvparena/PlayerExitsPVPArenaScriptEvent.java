package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArenaTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.slipcor.pvparena.events.PAExitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerExitsPVPArenaScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // pvparena player exits
    //
    // @Regex ^on pvparena player exits$
    //
    // @Triggers when a player exit a pvparena (won, loose, leave, disconnect etc.)
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

    public PAExitEvent event;
    public PVPArenaArenaTag arena;

    public PlayerExitsPVPArenaScriptEvent() {
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("pvparena player exits");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerExitsPVPArena(PAExitEvent event) {
        arena = new PVPArenaArenaTag(event.getArena());
        this.event = event;
        fire(event);
    }
}
