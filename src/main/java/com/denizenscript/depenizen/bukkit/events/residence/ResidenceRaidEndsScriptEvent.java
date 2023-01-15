package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceRaidEndEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ResidenceRaidEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence raid ends
    //
    // @Switch residence:<residence> to only process the event if the residence matches the input.
    //
    // @Triggers when a Residence raid ends.
    //
    // @Context
    // <context.residence> Returns a ResidenceTag of the residence that was being raided.
    //
    // @Plugin Depenizen, Residence
    //
    // @Group Depenizen
    //
    // -->

    public ResidenceRaidEndsScriptEvent() {
        registerCouldMatcher("residence raid ends");
        registerSwitches("residence");
    }

    public ResidenceRaidEndEvent event;
    public ResidenceTag residence;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("residence", residence)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "residence": return residence;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceRaidEndsScriptEvent(ResidenceRaidEndEvent event) {
        this.event = event;
        residence = new ResidenceTag(event.getRes());
        fire(event);
    }
}
