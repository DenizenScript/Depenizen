package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArena;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.events.PAJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
// @Plugin DepenizenBukkit, PVPArena
//
// -->

public class PlayerJoinsPVPArenaScriptEvent extends BukkitScriptEvent implements Listener {

    public static PlayerJoinsPVPArenaScriptEvent instance;
    public PAJoinEvent event;
    public PVPArenaArena arena;

    public PlayerJoinsPVPArenaScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("pvparena player joins");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "PlayerJoinsPVPArena";
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
    public void onPlayerJoinsPVPArena(PAJoinEvent event) {
        arena = new PVPArenaArena(event.getArena());
        this.event = event;
        fire(event);
    }
}
