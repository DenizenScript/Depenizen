package com.denizenscript.depenizen.bukkit.events.essentials;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // -->

    public static PlayerAFKStatusScriptEvent instance;
    public AfkStatusChangeEvent event;
    public ElementTag afk;

    public PlayerAFKStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("player goes afk")
                || lower.startsWith("player returns from afk")
                || lower.startsWith("player afk status changes");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(1).equals("goes") && afk.asBoolean()) {
            return true;
        }
        else if (path.eventArgLowerAt(1).equals("returns") && !afk.asBoolean()) {
            return true;
        }
        else {
            return path.eventArgLowerAt(1).equals("afk");
        }
    }

    @Override
    public String getName() {
        return "PlayerAFKStatus";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(PlayerTag.mirrorBukkitPlayer(event.getAffected().getBase()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("status")) {
            return afk;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(AfkStatusChangeEvent event) {
        afk = new ElementTag(event.getValue());
        this.event = event;
        fire(event);
    }
}
