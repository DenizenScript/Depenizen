package com.denizenscript.depenizen.bukkit.events.griefprevention;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GPClaimDeletedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // gp claim deleted
    //
    // @Triggers when a Grief Prevention claim is deleted.
    //
    // @Context
    // <context.claim> returns the GriefPreventionClaimTag being deleted.
    //
    // @Plugin Depenizen, GriefPrevention
    //
    // @Group Depenizen
    //
    // -->

    public GPClaimDeletedScriptEvent() {
        registerCouldMatcher("gp claim deleted");
    }

    public ClaimDeletedEvent event;

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "claim": new GriefPreventionClaimTag(event.getClaim());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onClaimDeleted(ClaimDeletedEvent event) {
        this.event = event;
        fire(event);
    }
}
