package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.ess3.api.events.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class EssentialsEvents implements Listener {

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

    // <--[event]
    // @Events
    // player god mode enabled
    // @Triggers when a player has god mode enabled.
    // @Context
    // <context.controller> returns the dPlayer that enabled god mode for this player, if any.
    // @Determine
    // "CANCELLED" to stop the player from having god mode enabled.
    // @Plugin Essentials
    // -->
    // <--[event]
    // @Events
    // player god mode disabled
    // @Triggers when a player has god mode disabled.
    // @Context
    // <context.controller> returns the dPlayer that disabled god mode for this player, if any.
    // @Determine
    // "CANCELLED" to stop the player from having god mode disabled.
    // @Plugin Essentials
    // -->
    @EventHandler
    public void godStatusChange(GodStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null)
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));

        List<String> events = new ArrayList<String>();
        if (event.getValue())
            events.add("player god mode enabled");
        else
            events.add("player god mode disabled");

        String determination = EventManager.doEvents(events, null,
                dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), context).toUpperCase();

        if (determination.equals("CANCELLED"))
            event.setCancelled(true);

    }

    // <--[event]
    // @Events
    // player jailed
    // @Triggers when a player is put into jail.
    // @Context
    // <context.controller> returns the dPlayer that jailed the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being jailed.
    // @Plugin Essentials
    // -->
    // <--[event]
    // @Events
    // player unjailed
    // @Triggers when a player is taken out of jail.
    // @Context
    // <context.controller> returns the dPlayer that unjailed the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being unjailed.
    // @Plugin Essentials
    // -->
    @EventHandler
    public void jailStatusChange(JailStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null)
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));

        List<String> events = new ArrayList<String>();
        if (event.getValue())
            events.add("player jailed");
        else
            events.add("player unjailed");

        String determination = EventManager.doEvents(events, null,
                dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), context).toUpperCase();

        if (determination.equals("CANCELLED"))
            event.setCancelled(true);

    }

    // <--[event]
    // @Events
    // player muted
    // @Triggers when a player is muted.
    // @Context
    // <context.controller> returns the dPlayer that muted the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being muted.
    // @Plugin Essentials
    // -->
    // <--[event]
    // @Events
    // player unmuted
    // @Triggers when a player is unmuted.
    // @Context
    // <context.controller> returns the dPlayer that unmuted the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being unmuted.
    // @Plugin Essentials
    // -->
    @EventHandler
    public void muteStatusChange(MuteStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null)
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));

        List<String> events = new ArrayList<String>();
        if (event.getValue())
            events.add("player muted");
        else
            events.add("player unmuted");

        String determination = EventManager.doEvents(events, null,
                dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), context).toUpperCase();

        if (determination.equals("CANCELLED"))
            event.setCancelled(true);

    }

    // TODO: add the rest of the events

}
