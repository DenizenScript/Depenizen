package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArenaTag;
import com.garbagemule.MobArena.events.NewWaveEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaWaveChangesScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mobarena arena wave changes
    // mobarena <arena> wave changes
    //
    // @Regex ^on mobarena [^\s]+ wave changes$
    //
    // @Triggers when a wave changes in a MobArena.
    //
    // @Context
    // <context.arena> Returns the arena in which the wave change occured.
    // <context.wave> Returns the number of the new wave.
    //
    // @Plugin Depenizen, MobArena
    //
    // -->

    public MobArenaWaveChangesScriptEvent() {
        instance = this;
    }

    public static MobArenaWaveChangesScriptEvent instance;
    public NewWaveEvent event;
    public MobArenaArenaTag arena;
    public ElementTag wave;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("mobarena")
                && path.eventArgLowerAt(2).equals("wave")
                && path.eventArgLowerAt(3).equals("changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String arenaname = path.eventArgLowerAt(2).replace("mobarena@", "");
        MobArenaArenaTag a = MobArenaArenaTag.valueOf(arenaname);
        if (!((arenaname.equals("arena") || (a != null && a.getArena() == event.getArena())) && event.getWave() != null)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "MobArenaWaveChanges";
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
    public void onMobArenaWaveChanges(NewWaveEvent event) {
        arena = new MobArenaArenaTag(event.getArena());
        wave = new ElementTag(event.getWaveNumber());
        this.event = event;
        fire(event);
    }
}
