package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// residence player enters residence
// residence player enters <residence>
//
// @Regex ^on residence player enters [^\s]+$
//
// @Cancellable false
//
// @Triggers when a player enters a Residence.
//
// @Context
// <context.residence> Returns the Residence the player entered.
//
// @Plugin DepenizenBukkit, Residence
//
// -->

public class PlayerEntersResidenceScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerEntersResidenceScriptEvent() {
        instance = this;
    }

    public static PlayerEntersResidenceScriptEvent instance;
    public ResidenceChangedEvent event;
    public dResidence residence;


    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("residence player enters");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String name = CoreUtilities.getXthArg(3, CoreUtilities.toLowerCase(s));
        dResidence eventResidence = event.getTo() != null ? new dResidence(event.getTo()) : null;
        if (name.equals("residence") && eventResidence != null) {
            return true;
        }
        dResidence givenResidence = dResidence.valueOf(name);
        if (eventResidence != null && givenResidence != null && eventResidence.equals(givenResidence)) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "ResidencePlayerEntersResidence";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ResidenceChangedEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new dPlayer(event.getPlayer()), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("residence")) {
            return residence;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidencePlayerEntersResidence(ResidenceChangedEvent event) {
        if (event.getTo() == null) {
            return;
        }
        residence = new dResidence(event.getTo());
        this.event = event;
        fire();
    }
}
