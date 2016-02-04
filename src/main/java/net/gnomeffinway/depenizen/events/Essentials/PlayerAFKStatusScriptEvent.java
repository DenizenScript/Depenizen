package net.gnomeffinway.depenizen.events.Essentials;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// on player goes afk
// on player returns from afk
// on player afk status changes
//
// @Regex ^on player [^\s]+$
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

public class PlayerAFKStatusScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerAFKStatusScriptEvent instance;
    public AfkStatusChangeEvent event;
    public Element afk;

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
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        if (lower.startsWith("player goes afk") && afk.asBoolean()) {
            return true;
        }
        else if (lower.startsWith("player returns from afk") && !afk.asBoolean()) {
            return true;
        }
        else {
            return lower.startsWith("player afk status changes");
        }
    }

    @Override
    public String getName() {
        return "PlayerAFKStatus";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        AfkStatusChangeEvent.getHandlerList().unregister(this);
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
            return afk;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(AfkStatusChangeEvent event) {
        afk = new Element(event.getValue());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
