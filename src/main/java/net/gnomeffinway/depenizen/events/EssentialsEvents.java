package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class EssentialsEvents implements Listener {

    public EssentialsEvents(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }

    // <--[event]
    // @Events
    // player goes afk
    // @Triggers when a player goes AFK.
    // @Context
    // None
    // @Determine
    // "CANCELLED" to stop the player from being marked as AFK.
    // @Plugin Essentials
    // -->
    // <--[event]
    // @Events
    // player returns from afk
    // @Triggers when a player returns from being AFK.
    // @Context
    // None
    // @Determine
    // "CANCELLED" to stop the player from being marked as back.
    // @Plugin Essentials
    // -->
    @EventHandler
    public void afkStatusChange(AfkStatusChangeEvent event) {

        List<String> events = new ArrayList<String>();
        if (event.getValue())
            events.add("player goes afk");
        else
            events.add("player returns from afk");

        String determination = EventManager.doEvents(events, null,
                dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null).toUpperCase();

        if (determination.equals("CANCELLED"))
            event.setCancelled(true);

    }

    // TODO: add the rest of the events

}
