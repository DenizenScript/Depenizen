package net.gnomeffinway.depenizen.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dList;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.scripts.containers.core.WorldScriptHelper;
import net.gnomeffinway.depenizen.Depenizen;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.events.PAStartEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PVPArenaEvents implements Listener {
    
    public PVPArenaEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    // <--[event]
    // @events arena starts
    // @triggers when an arena starts a round.
    // @context
    // <context.arena> returns the arena name.
    // <context.fighters> returns a dList of the fighters in the round.
    // @plugin PvP Arena
    // -->
    @EventHandler
    public void onArenaStarts(PAStartEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        Arena arena = event.getArena();
        
        context.put("arena", new Element(arena.getName()));
        
        ArrayList<dPlayer> fighters = new ArrayList<dPlayer>();
        for (ArenaPlayer player : event.getArena().getFighters())
            fighters.add(new dPlayer(player.get()));
        
        context.put("fighters", new dList(fighters));
        
        WorldScriptHelper.doEvents(Arrays.asList
                ("arena starts"),
                null, null, context);
        
    }

}
