package net.gnomeffinway.depenizen.events.MobArena;

import com.garbagemule.MobArena.events.ArenaEndEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.gnomeffinway.depenizen.objects.mobarena.mobarena;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

public class MobArenaEndsScriptEvent extends BukkitScriptEvent implements Listener {

    public MobArenaEndsScriptEvent() {
        instance = this;
    }

    public MobArenaEndsScriptEvent instance;
    public ArenaEndEvent event;
    public mobarena arena;
    public Element wave;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("mobarena") && CoreUtilities.getXthArg(2, lower).equals("ends");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String arenaname = CoreUtilities.getXthArg(2, lower).replace("mobarena@", "");
        mobarena a = mobarena.valueOf(arenaname);
        return arena.equals("arena") || (a != null && a.getArena() == event.getArena());
    }

    @Override
    public String getName() {
        return "MobArenaEnds";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ArenaEndEvent.getHandlerList().unregister(this);
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
        arena = new mobarena(event.getArena());
        wave = new Element(event.getArena().getWaveManager().getWaveNumber());
        this.event = event;
        fire();
    }
}
