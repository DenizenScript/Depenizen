package com.denizenscript.depenizen.bukkit.events.mobarena;

import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.garbagemule.MobArena.events.ArenaEndEvent;
import com.denizenscript.depenizen.bukkit.objects.mobarena.MobArenaArenaTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MobArenaEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // mobarena <'arena'> ends
    //
    // @Triggers when a mobarena ends.
    //
    // @Context
    // <context.arena> Returns the arena which ended.
    // <context.wave> Returns the number of the final wave.
    //
    // @Plugin Depenizen, MobArena
    //
    // @Group Depenizen
    //
    // -->

    public MobArenaEndsScriptEvent() {
        registerCouldMatcher("mobarena <'arena'> ends");
    }

    public ArenaEndEvent event;
    public MobArenaArenaTag arena;
    public ElementTag wave;

    @Override
    public boolean matches(ScriptPath path) {
        String arenaname = path.eventArgLowerAt(1);
        MobArenaArenaTag a = MobArenaArenaTag.valueOf(arenaname);
        if (!arenaname.equals("arena") && (a == null || !CoreUtilities.equalsIgnoreCase(a.getArena().arenaName(), arenaname))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "arena" -> arena;
            case "wave" -> wave;
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onMobArenaEnds(ArenaEndEvent event) {
        arena = new MobArenaArenaTag(event.getArena());
        wave = new ElementTag(event.getArena().getWaveManager().getWaveNumber());
        this.event = event;
        fire(event);
    }
}
