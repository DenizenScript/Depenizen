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
import net.ess3.api.events.MuteStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// on player muted
// on player unmuted
// on player un-muted
// on player mute status changes
//
// @Regex ^on player [^\s]+$
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

public class PlayerMuteStatusScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerMuteStatusScriptEvent instance;
    public MuteStatusChangeEvent event;
    public Element muted;

    public PlayerMuteStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("player mute")
                || lower.startsWith("player unmuted")
                || lower.startsWith("player un-muted");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String status = CoreUtilities.getXthArg(1, lower);
        if (status.equals("muted") && muted.asBoolean()) {
            return true;
        }
        else if ((status.equals("unmuted") || status.equals("un-muted")) && !muted.asBoolean()) {
            return true;
        }
        else {
            return lower.startsWith("player mute status changes");
        }
    }

    @Override
    public String getName() {
        return "PlayerMuteStatus";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MuteStatusChangeEvent.getHandlerList().unregister(this);
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
            return muted;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(MuteStatusChangeEvent event) {
        muted = new Element(event.getValue());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
