package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// residence player exits residence
// residence player exits <residence>
//
// @Regex ^on residence player exits [^\s]+$
//
// @Cancellable false
//
// @Triggers when a player exits a Residence.
//
// @Context
// <context.residence> Returns the Residence the player exited.
//
// @Plugin DepenizenBukkit, Residence
//
// -->

public class PlayerExitsResidenceScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerExitsResidenceScriptEvent() {
        instance = this;
    }

    public static PlayerExitsResidenceScriptEvent instance;
    public ResidenceChangedEvent event;
    public dResidence residence;


    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("residence player exits");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
        dResidence eventResidence = event.getFrom() != null ? new dResidence(event.getFrom()) : null;
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
        return "ResidencePlayerExitsResidence";
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
    public void onResidencePlayerExitsResidence(ResidenceChangedEvent event) {
        if (event.getFrom() == null) {
            return;
        }
        residence = new dResidence(event.getFrom());
        this.event = event;
        fire();
    }
}
