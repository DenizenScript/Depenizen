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

public class ResidenceGetsDeletedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // residence gets deleted
    // residence get deleted
    //
    // @Switch cause:<cause> to only process the event if the cause equals specified cause
    //
    // @Triggers when a player deletes a Residence.
    //
    // @Context
    // <context.cause> Returns the cause of deletion. ( Available causes: PLAYER_DELETE, OTHER, LEASE_EXPIRE )
    // <context.residence> Returns the ResidenceTag of deleted residence.
    //
    // @Plugin Depenizen, Residence
    //
    // @Player only when the cause is PLAYER_DELETE.
    //
    // @Group Depenizen
    //
    // -->

    public ResidenceGetsDeletedScriptEvent() {
        registerCouldMatcher("residence gets deleted"); registerCouldMatcher("residence get deleted");
        registerSwitches("cause");
    }

    public ResidenceDeleteEvent event;
    public String cause;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericSwitchCheck(path, "cause", cause)) {
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
            case "cause": return new ElementTag(cause);
            case "residence": return new ResidenceTag(event.getResidence());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onResidenceGetsDeleted(ResidenceDeleteEvent event) {
        if (event.getResidence() == null) {
            return;
        }
        cause = event.getCause().name();
        this.event = event;
        fire(event);
    }

}
