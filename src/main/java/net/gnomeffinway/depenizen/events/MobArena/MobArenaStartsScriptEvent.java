package net.gnomeffinway.depenizen.events.MobArena;

import com.garbagemule.MobArena.events.ArenaStartEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.gnomeffinway.depenizen.objects.mobarena.MobArenaArena;
import org.bukkit.Bukkit;
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
// @Plugin Depenizen, MobArena
//
// -->

public class MobArenaStartsScriptEvent extends BukkitScriptEvent implements Listener {

    public MobArenaStartsScriptEvent() {
        instance = this;
    }

    public MobArenaStartsScriptEvent instance;
    public ArenaStartEvent event;
    public MobArenaArena arena;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("mobarena") && CoreUtilities.getXthArg(2, lower).equals("starts");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String arenaname = CoreUtilities.getXthArg(2, lower).replace("mobarena@", "");
        MobArenaArena a = MobArenaArena.valueOf(arenaname);
        return arena.equals("arena") || (a != null && a.getArena() == event.getArena());
    }

    @Override
    public String getName() {
        return "MobArenaStarts";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ArenaStartEvent.getHandlerList().unregister(this);
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
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
