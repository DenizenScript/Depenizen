package com.denizenscript.depenizen.bukkit.events.essentials;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.ess3.api.events.JailStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
// @Plugin DepenizenBukkit, Essentials
//
// -->

public class PlayerJailStatusScriptEvent extends BukkitScriptEvent implements Listener {

    public static PlayerJailStatusScriptEvent instance;
    public JailStatusChangeEvent event;
    public Element jailed;

    public PlayerJailStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("player jail")
                || lower.startsWith("player unjailed")
                || lower.startsWith("player un-jailed");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(1);
        if (status.equals("jailed") && jailed.asBoolean()) {
            return true;
        }
        else if ((status.equals("unjailed") || status.equals("un-jailed")) && !jailed.asBoolean()) {
            return true;
        }
        else {
            return status.equals("status");
        }
    }

    @Override
    public String getName() {
        return "PlayerJailStatus";
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
            return jailed;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(JailStatusChangeEvent event) {
        jailed = new Element(event.getValue());
        this.event = event;
        fire(event);
    }
}
