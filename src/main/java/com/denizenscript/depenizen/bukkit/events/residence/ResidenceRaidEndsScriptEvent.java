package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceRaidEndEvent;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ResidenceRaidEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence raid end
    // residence raid ends
    //
    // @Switch residence:<residence_name> to only process the event if the residence name equals specified name
    //
    // @Triggers when a raiding a Residence ends.
    //
    // @Context
    // <context.residence> Returns a ResidenceTag of residence that was being raided.
    //
    // @Plugin Depenizen, Residence
    //
    // @Group Depenizen
    //
    // -->

    public ResidenceRaidEndsScriptEvent() {
        registerCouldMatcher("residence raid ends"); registerCouldMatcher("residence raid end");
        registerSwitches("residence");
    }

    public ResidenceRaidEndEvent event;
    public ClaimedResidence residence;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "residence", residence.getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "residence": return new ResidenceTag(residence);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceRaidEndsScriptEvent(ResidenceRaidEndEvent event) {
        if (event.getRes() == null) {
            return;
        }
        residence = event.getRes();
        this.event = event;
        fire(event);
    }
}
