package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.events.PAStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PVPArenaEvents implements Listener {

    // <--[event]
    // @Events
    // arena starts
    // @Triggers when an arena starts a round.
    // @Context
    // <context.arena> returns the arena name.
    // <context.fighters> returns a dList of the fighters in the round.
    // @Plugin Depenizen, PvP Arena
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

        OldEventManager.doEvents(Arrays.asList
                        ("pvparena starts"),
                null, context);

    }
}
