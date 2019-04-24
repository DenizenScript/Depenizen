package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArena;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
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
    // @Plugin DepenizenBukkit, MobArena
    //
    // -->

    public MobArenaEndsScriptEvent() {
        instance = this;
    }

    public static MobArenaEndsScriptEvent instance;
    public ArenaEndEvent event;
    public MobArenaArena arena;
    public Element wave;

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
    public void onMobArenaEnds(ArenaEndEvent event) {
        arena = new MobArenaArena(event.getArena());
        wave = new Element(event.getArena().getWaveManager().getWaveNumber());
        this.event = event;
        fire(event);
    }
}
