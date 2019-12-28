package com.denizenscript.depenizen.bukkit.events.pvparena;

import com.denizenscript.depenizen.bukkit.objects.pvparena.PVPArenaArenaTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.events.PAStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PVPArenaStartsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // pvparena starts
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

    public static PVPArenaStartsScriptEvent instance;
    public PAStartEvent event;
    public ListTag fighters;
    public PVPArenaArenaTag arena;

    public PVPArenaStartsScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("pvparena starts");
    }

    @Override
    public String getName() {
        return "PVPArenaStarts";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
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
        fighters = new ListTag();
        for (ArenaPlayer p : event.getArena().getFighters()) {
            fighters.addObject(new PlayerTag(p.get()));
        }
        arena = new PVPArenaArenaTag(event.getArena());
        this.event = event;
        fire(event);
    }
}
