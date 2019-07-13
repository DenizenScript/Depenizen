package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArenaTag;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // @Cancellable false
    //
    // @Triggers when a player exit a pvparena (won, loose, leave, disconnect etc.)
    //
    // @Context
    // <context.arena> returns the arena denizen object.
    //
    // @Plugin Depenizen, PVPArena
    //
    // -->

    public static PlayerExitsPVPArenaScriptEvent instance;
    public PAExitEvent event;
    public PVPArenaArenaTag arena;

    public PlayerExitsPVPArenaScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("pvparena player exits");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "PlayerExitsPVPArena";
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
    public void onPlayerExitsPVPArena(PAExitEvent event) {
        arena = new PVPArenaArenaTag(event.getArena());
        cancelled = false;
        this.event = event;
        fire(event);
    }
}
