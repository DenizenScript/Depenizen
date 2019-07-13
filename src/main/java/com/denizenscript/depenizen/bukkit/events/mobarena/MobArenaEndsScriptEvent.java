package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mobarena arena end
    // mobarena <arena> ends
    //
    // @Regex ^on mobarena [^\s]+ ends$
    //
    // @Cancellable false
    //
    // @Triggers when a mobarena ends.
    //
    // @Context
    // <context.arena> Returns the arena which ended.
    // <context.wave> Returns the number of the final wave.
    //
    // @Plugin Depenizen, MobArena
    //
    // -->

    public MobArenaEndsScriptEvent() {
        instance = this;
    }

    public static MobArenaEndsScriptEvent instance;
    public ArenaEndEvent event;
    public MobArenaArena arena;
    public ElementTag wave;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("mobarena") && CoreUtilities.xthArgEquals(2, lower, "ends");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String arenaname = path.eventArgLowerAt(2);
        MobArenaArena a = MobArenaArena.valueOf(arenaname);
        return arenaname.equals("arena") || (a != null && a.getArena() == event.getArena());
    }

    @Override
    public String getName() {
        return "MobArenaEnds";
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
    public ObjectTag getContext(String name) {
        if (name.equals("arena")) {
            return arena;
        }
        else if (name.equals("wave")) {
            return wave;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMobArenaEnds(ArenaEndEvent event) {
        arena = new MobArenaArena(event.getArena());
        wave = new ElementTag(event.getArena().getWaveManager().getWaveNumber());
        this.event = event;
        fire(event);
    }
}
