package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceRaidEndEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ResidenceRaidEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence raid ends
    //
    // @Switch residence:<residence_name> to only process the event if the residence name matches specified name.
    //
    // @Triggers when a raiding a Residence ends.
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

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("residence", new ElementTag(event.getRes().getName()))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "residence": return new ResidenceTag(event.getRes());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceRaidEndsScriptEvent(ResidenceRaidEndEvent event) {
        this.event = event;
        fire(event);
    }
}
