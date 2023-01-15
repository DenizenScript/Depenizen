package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceCreationEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerCreatesResidenceScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence player creates residence
    //
    // @Cancellable true
    //
    // @Triggers when a player creates a Residence.
    //
    // @Context
    // <context.residence> Returns the created ResidenceTag.
    //
    // @Plugin Depenizen, Residence
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerCreatesResidenceScriptEvent() {
        registerCouldMatcher("residence player creates residence");
    }

    public ResidenceCreationEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "residence": return new ResidenceTag(event.getResidence());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidencePlayerCreatesResidence(ResidenceCreationEvent event) {
        this.event = event;
        fire(event);
    }
}
