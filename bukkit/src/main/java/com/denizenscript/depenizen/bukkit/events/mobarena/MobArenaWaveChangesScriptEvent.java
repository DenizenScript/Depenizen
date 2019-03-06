package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import com.garbagemule.MobArena.events.NewWaveEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// mobarena arena wave changes
// mobarena <arena> wave changes
//
// @Regex ^on mobarena [^\s]+ wave changes$
//
// @Cancellable false
//
// @Triggers when a wave changes in a MobArena.
//
// @Context
// <context.arena> Returns the arena in which the wave change occured.
// <context.wave> Returns the number of the new wave.
//
// @Plugin DepenizenBukkit, MobArena
//
// -->

public class MobArenaWaveChangesScriptEvent extends BukkitScriptEvent implements Listener {

    public MobArenaWaveChangesScriptEvent() {
        instance = this;
    }

    public static MobArenaWaveChangesScriptEvent instance;
    public NewWaveEvent event;
    public MobArenaArena arena;
    public Element wave;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("mobarena")
                && CoreUtilities.getXthArg(2, lower).equals("wave")
                && CoreUtilities.getXthArg(3, lower).equals("changes");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String arenaname = CoreUtilities.getXthArg(2, lower).replace("mobarena@", "");
        MobArenaArena a = MobArenaArena.valueOf(arenaname);
        return (arenaname.equals("arena") || (a != null && a.getArena() == event.getArena()))
                && event.getWave() != null;
    }

    @Override
    public String getName() {
        return "MobArenaWaveChanges";
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
        else if (name.equals("wave")) {
            return wave;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMobArenaWaveChanges(NewWaveEvent event) {
        arena = new MobArenaArena(event.getArena());
        wave = new Element(event.getWaveNumber());
        this.event = event;
        fire();
    }
}
