package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.events.PALeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeavesPVPArenaScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // pvparena player leaves
    //
    // @Regex ^on pvparena player leaves$
    //
    // @Cancellable false
    //
    // @Triggers when a player leaves a pvparena.
    //
    // @Context
    // <context.arena> returns the arena denizen object.
    //
    // @Plugin Depenizen, PVPArena
    //
    // -->

    public static PlayerLeavesPVPArenaScriptEvent instance;
    public PALeaveEvent event;
    public PVPArenaArena arena;

    public PlayerLeavesPVPArenaScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("pvparena player leaves");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "PlayerLeavesPVPArena";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
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
    public void onPlayerLeavesPVPArena(PALeaveEvent event) {
        arena = new PVPArenaArena(event.getArena());
        cancelled = false;
        this.event = event;
        fire(event);
    }
}
