package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.garbagemule.MobArena.events.ArenaStartEvent;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// mobarena arena starts
// mobarena <arena> starts
//
// @Regex ^on mobarena [^\s]+ starts$
//
// @Cancellable true
//
// @Triggers when a mobarena starts.
//
// @Context
// <context.arena> Returns the arena which started.
//
// @Plugin DepenizenBukkit, MobArena
//
// -->

public class MobArenaStartsScriptEvent extends BukkitScriptEvent implements Listener {

    public MobArenaStartsScriptEvent() {
        instance = this;
    }

    public static MobArenaStartsScriptEvent instance;
    public ArenaStartEvent event;
    public MobArenaArena arena;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("mobarena") && CoreUtilities.xthArgEquals(2, lower, "starts");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String arenaname = path.eventArgLowerAt(2).replace("mobarena@", "");
        MobArenaArena a = MobArenaArena.valueOf(arenaname);
        return arenaname.equals("arena") || (a != null && a.getArena() == event.getArena());
    }

    @Override
    public String getName() {
        return "MobArenaStarts";
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
        if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMobArenaStarts(ArenaStartEvent event) {
        arena = new MobArenaArena(event.getArena());
        this.event = event;
        fire(event);
    }
}
