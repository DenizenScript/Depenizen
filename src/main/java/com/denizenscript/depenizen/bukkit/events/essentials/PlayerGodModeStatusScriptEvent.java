package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.ess3.api.events.GodStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerGodModeStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player god mode enabled
    // player god mode disabled
    // player god mode status changes
    //
    // @Regex ^on player god mode (enabled|disabled|status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player's god mode status changes.
    //
    // @Context
    // <context.status> Returns the player's god mode status.
    //
    // @Plugin Depenizen, Essentials
    //
    // -->

    public static PlayerGodModeStatusScriptEvent instance;
    public GodStatusChangeEvent event;

    public PlayerGodModeStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("player god mode");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(3);
        if (status.equals("enabled") && event.getValue()) {
            return true;
        }
        else if (status.equals("disabled") && !event.getValue()) {
            return true;
        }
        else {
            return status.equals("status");
        }
    }

    @Override
    public String getName() {
        return "PlayerGodModeStatus";
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
    public void onPlayerAFKStatus(GodStatusChangeEvent event) {
        this.event = event;
        fire(event);
    }
}
