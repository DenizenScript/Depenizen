package com.denizenscript.depenizen.bukkit.events.essentials;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    public Element god;

    public PlayerGodModeStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("player god mode");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(3);
        if (status.equals("enabled") && god.asBoolean()) {
            return true;
        }
        else if (status.equals("disabled") && !god.asBoolean()) {
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
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("status")) {
            return god;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(GodStatusChangeEvent event) {
        god = new Element(event.getValue());
        this.event = event;
        fire(event);
    }
}
