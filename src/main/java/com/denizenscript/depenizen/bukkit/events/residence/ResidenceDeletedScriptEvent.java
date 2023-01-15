package com.denizenscript.depenizen.bukkit.events.residence;

import com.bekvon.bukkit.residence.event.ResidenceDeleteEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ResidenceDeletedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence deleted
    //
    // @Switch cause:<cause> to only process the event if the cause of deletion matches.
    //
    // @Triggers when a Residence gets deleted.
    //
    // @Context
    // <context.cause> Returns the cause of deletion. Can be: PLAYER_DELETE, OTHER or LEASE_EXPIRE
    // <context.residence> Returns the ResidenceTag of the deleted residence.
    //
    // @Plugin Depenizen, Residence
    //
    // @Player only when the cause is PLAYER_DELETE.
    //
    // @Group Depenizen
    //
    // -->

    public ResidenceDeletedScriptEvent() {
        registerCouldMatcher("residence deleted");
        registerSwitches("cause");
    }

    public ResidenceDeleteEvent event;
    public ElementTag cause;

    @Override
    public boolean matches(ScriptPath path) {
        if (!path.tryObjectSwitch("cause", cause)) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "cause": return cause;
            case "residence": return new ResidenceTag(event.getResidence());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceDeleted(ResidenceDeleteEvent event) {
        this.event = event;
        cause = new ElementTag(event.getCause());
        fire(event);
    }

}
