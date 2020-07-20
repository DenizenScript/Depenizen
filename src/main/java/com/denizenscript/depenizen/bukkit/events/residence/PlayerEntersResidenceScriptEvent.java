package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceChangedEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
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
    // @Triggers when a player enters a Residence.
    //
    // @Context
    // <context.residence> Returns the Residence the player entered.
    //
    // @Plugin Depenizen, Residence
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerEntersResidenceScriptEvent() {
        instance = this;
    }

    public static PlayerEntersResidenceScriptEvent instance;
    public ResidenceChangedEvent event;
    public ResidenceTag residence;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("residence player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String name = path.eventArgLowerAt(3);
        if (!name.equals("residence") && !residence.equals(ResidenceTag.valueOf(name))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "ResidencePlayerEntersResidence";
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
