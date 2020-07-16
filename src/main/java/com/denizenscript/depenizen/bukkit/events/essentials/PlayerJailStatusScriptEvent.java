package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.ess3.api.events.JailStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJailStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player jailed
    // player unjailed
    // player un-jailed
    // player jail status changes
    //
    // @Regex ^on player (jailed|unjailed|un-jailed|jail status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player's jail status changes.
    //
    // @Context
    // <context.status> Returns the player's jail status.
    //
    // @Plugin Depenizen, Essentials
    //
    // -->

    public static PlayerJailStatusScriptEvent instance;
    public JailStatusChangeEvent event;

    public PlayerJailStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player jail")
                || path.eventLower.startsWith("player unjailed")
                || path.eventLower.startsWith("player un-jailed");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(1);
        if (status.equals("jailed")) {
            if (!event.getValue()) {
                return false;
            }
        }
        else if (status.equals("unjailed") || status.equals("un-jailed")) {
            if (event.getValue()) {
                return false;
            }
        }
        else if (!status.equals("status")) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "PlayerJailStatus";
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
    public void onPlayerAFKStatus(JailStatusChangeEvent event) {
        this.event = event;
        fire(event);
    }
}
