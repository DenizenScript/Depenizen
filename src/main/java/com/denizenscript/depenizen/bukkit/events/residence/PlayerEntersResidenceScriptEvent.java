package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerEntersResidenceScriptEvent extends BukkitScriptEvent implements Listener {

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
    // @Plugin Depenizen, Residence
    //
    // -->

    public PlayerEntersResidenceScriptEvent() {
        instance = this;
    }

    public static PlayerEntersResidenceScriptEvent instance;
    public ResidenceChangedEvent event;
    public ResidenceTag residence;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("residence player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
        ResidenceTag eventResidence = event.getTo() != null ? new ResidenceTag(event.getTo()) : null;
        if (name.equals("residence") && eventResidence != null) {
            return true;
        }
        ResidenceTag givenResidence = ResidenceTag.valueOf(name);
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
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getPlayer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
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
        residence = new ResidenceTag(event.getTo());
        this.event = event;
        fire(event);
    }
}
