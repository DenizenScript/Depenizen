package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
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
    // @Plugin DepenizenBukkit, PVPArena
    //
    // -->

    public static PlayerExitsPVPArenaScriptEvent instance;
    public PAExitEvent event;
    public PVPArenaArena arena;

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
        return new BukkitScriptEntryData(new dPlayer(event.getPlayer()), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerExitsPVPArena(PAExitEvent event) {
        arena = new PVPArenaArena(event.getArena());
        cancelled = false;
        this.event = event;
        fire(event);
    }
}
