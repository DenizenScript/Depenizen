package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.ess3.api.events.MuteStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMuteStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player muted
    // player unmuted
    // player un-muted
    // player mute status changes
    //
    // @Regex ^on player (muted|unmuted|un-muted|mute status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player is muted or un-muted.
    //
    // @Context
    // <context.status> Returns whether the player is muted.
    //
    // @Plugin Depenizen, Essentials
    //
    // -->

    public static PlayerMuteStatusScriptEvent instance;
    public MuteStatusChangeEvent event;

    public PlayerMuteStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player mute")
                || path.eventLower.startsWith("player unmuted")
                || path.eventLower.startsWith("player un-muted");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(1);
        if (status.equals("muted") && !event.getValue()) {
            return false;
        }
        if ((status.equals("unmuted") || status.equals("un-muted")) && event.getValue()) {
            return false;
        }
        if (!status.equals("status")) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "PlayerMuteStatus";
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
    public void onPlayerAFKStatus(MuteStatusChangeEvent event) {
        this.event = event;
        fire(event);
    }
}
