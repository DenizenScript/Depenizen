package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.events.PAStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// on pvparena starts
//
// @Regex ^on pvparena starts$
//
// @Cancellable true
//
// @Triggers when a pvparena starts.
//
// @Context
// <context.fighters> returns a list of all fighters in the arena.
//
// @Plugin Depenizen, PVPArena
//
// -->

public class PVPArenaStartsScriptEvent extends BukkitScriptEvent implements Listener {

    public static PVPArenaStartsScriptEvent instance;
    public PAStartEvent event;
    public dList fighters;
    public PVPArenaArena arena;

    public PVPArenaStartsScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("pvparena starts");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "PVPArenaStarts";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PAStartEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("fighters")) {
            return fighters;
        }
        else if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPVPArenaStart(PAStartEvent event) {
        fighters = new dList();
        for (ArenaPlayer p : event.getArena().getFighters()) {
            fighters.add(new dPlayer(p.get()).identify());
        }
        arena = new PVPArenaArena(event.getArena());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
