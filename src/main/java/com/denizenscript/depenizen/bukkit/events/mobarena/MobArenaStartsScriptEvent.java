package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.garbagemule.MobArena.events.ArenaStartEvent;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArenaTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaStartsScriptEvent extends BukkitScriptEvent implements Listener {

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
    // @Plugin Depenizen, MobArena
    //
    // -->

    public MobArenaStartsScriptEvent() {
        instance = this;
    }

    public static MobArenaStartsScriptEvent instance;
    public ArenaStartEvent event;
    public MobArenaArenaTag arena;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventArgLowerAt(0).equals("mobarena") && path.eventArgLowerAt(2).equals("starts");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String arenaname = path.eventArgLowerAt(1);
        MobArenaArenaTag a = MobArenaArenaTag.valueOf(arenaname);
        if (!arenaname.equals("arena") && (a == null || !CoreUtilities.toLowerCase(a.getArena().arenaName()).equals(arenaname))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "MobArenaStarts";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("arena")) {
            return arena;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMobArenaStarts(ArenaStartEvent event) {
        arena = new MobArenaArenaTag(event.getArena());
        this.event = event;
        fire(event);
    }
}
