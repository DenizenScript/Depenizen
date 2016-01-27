package net.gnomeffinway.depenizen.events;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.OldEventManager;
import net.aufdemrand.denizencore.objects.dObject;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.GodStatusChangeEvent;
import net.ess3.api.events.JailStatusChangeEvent;
import net.ess3.api.events.MuteStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EssentialsEvents implements Listener {

    // <--[event]
    // @Events
    // player goes afk
    // @Triggers when a player goes AFK.
    // @Context
    // None
    // @Determine
    // "CANCELLED" to stop the player from being marked as AFK.
    // @Plugin Depenizen, Essentials
    // -->
    // <--[event]
    // @Events
    // player returns from afk
    // @Triggers when a player returns from being AFK.
    // @Context
    // None
    // @Determine
    // "CANCELLED" to stop the player from being marked as back.
    // @Plugin Depenizen, Essentials
    // -->
    @EventHandler
    public void afkStatusChange(AfkStatusChangeEvent event) {

        List<String> events = new ArrayList<String>();
        if (event.getValue()) {
            events.add("player goes afk");
        }
        else {
            events.add("player returns from afk");
        }

        List<String> determinations = OldEventManager.doEvents(events,
                new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null), null);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }

    }

    // <--[event]
    // @Events
    // player god mode enabled
    // @Triggers when a player has god mode enabled.
    // @Context
    // <context.controller> returns the dPlayer that enabled god mode for this player, if any.
    // @Determine
    // "CANCELLED" to stop the player from having god mode enabled.
    // @Plugin Depenizen, Essentials
    // -->
    // <--[event]
    // @Events
    // player god mode disabled
    // @Triggers when a player has god mode disabled.
    // @Context
    // <context.controller> returns the dPlayer that disabled god mode for this player, if any.
    // @Determine
    // "CANCELLED" to stop the player from having god mode disabled.
    // @Plugin Depenizen, Essentials
    // -->
    @EventHandler
    public void godStatusChange(GodStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null) {
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));
        }

        List<String> events = new ArrayList<String>();
        if (event.getValue()) {
            events.add("player god mode enabled");
        }
        else {
            events.add("player god mode disabled");
        }

        List<String> determinations = OldEventManager.doEvents(events,
                new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }
    }

    // <--[event]
    // @Events
    // player jailed
    // @Triggers when a player is put into jail.
    // @Context
    // <context.controller> returns the dPlayer that jailed the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being jailed.
    // @Plugin Depenizen, Essentials
    // -->
    // <--[event]
    // @Events
    // player unjailed
    // @Triggers when a player is taken out of jail.
    // @Context
    // <context.controller> returns the dPlayer that unjailed the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being unjailed.
    // @Plugin Depenizen, Essentials
    // -->
    @EventHandler
    public void jailStatusChange(JailStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null) {
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));
        }

        List<String> events = new ArrayList<String>();
        if (event.getValue()) {
            events.add("player jailed");
        }
        else {
            events.add("player unjailed");
        }

        List<String> determinations = OldEventManager.doEvents(events,
                new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }
    }

    // <--[event]
    // @Events
    // player muted
    // @Triggers when a player is muted.
    // @Context
    // <context.controller> returns the dPlayer that muted the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being muted.
    // @Plugin Depenizen, Essentials
    // -->
    // <--[event]
    // @Events
    // player unmuted
    // @Triggers when a player is unmuted.
    // @Context
    // <context.controller> returns the dPlayer that unmuted the player, if any.
    // @Determine
    // "CANCELLED" to stop the player from being unmuted.
    // @Plugin Depenizen, Essentials
    // -->
    @EventHandler
    public void muteStatusChange(MuteStatusChangeEvent event) {

        Map<String, dObject> context = new HashMap<String, dObject>();
        if (event.getController() != null) {
            context.put("controller", dPlayer.mirrorBukkitPlayer(event.getController().getBase()));
        }

        List<String> events = new ArrayList<String>();
        if (event.getValue()) {
            events.add("player muted");
        }
        else {
            events.add("player unmuted");
        }

        List<String> determinations = OldEventManager.doEvents(events,
                new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null), context);

        for (String determination : determinations) {
            determination = determination.toUpperCase();
            if (determination.equals("CANCELLED")) {
                event.setCancelled(true);
            }
        }
    }

    // TODO: add the rest of the events
}
