package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAFKStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player goes afk
    // player returns from afk
    // player afk status changes
    //
    // @Regex ^on player (goes afk|returns from afk|afk status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player's afk status changes.
    //
    // @Context
    // <context.status> Returns the player's afk status.
    //
    // @Plugin Depenizen, Essentials
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public static PlayerAFKStatusScriptEvent instance;
    public AfkStatusChangeEvent event;

    public PlayerAFKStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player goes afk")
                || path.eventLower.startsWith("player returns from afk")
                || path.eventLower.startsWith("player afk status changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(1).equals("goes")) {
            if (!event.getValue()) {
                return false;
            }
        }
        else if (path.eventArgLowerAt(1).equals("returns")) {
            if (event.getValue()) {
                return false;
            }
        }
        else if (!path.eventArgLowerAt(1).equals("afk")) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "PlayerAFKStatus";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(PlayerTag.mirrorBukkitPlayer(event.getAffected().getBase()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("status")) {
            return new ElementTag(event.getValue());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(AfkStatusChangeEvent event) {
        this.event = event;
        fire(event);
    }
}
