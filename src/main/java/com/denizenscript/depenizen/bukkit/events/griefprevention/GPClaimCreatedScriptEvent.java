package com.denizenscript.depenizen.bukkit.events.griefprevention;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GPClaimCreatedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // gp claim created
    //
    // @Cancellable true
    //
    // @Triggers when a Grief Prevention claim is created.
    //
    // @Context
    // <context.claim> returns the GriefPreventionClaimTag that is being created.
    // <context.source_type> returns the source of the creation. Can be PLAYER or SERVER.
    //
    // @Plugin Depenizen, GriefPrevention
    //
    // @Player when source_type is player.
    //
    // @Group Depenizen
    //
    // -->

    public GPClaimCreatedScriptEvent() {
        registerCouldMatcher("gp claim created");
    }

    public ClaimCreatedEvent event;
    public String sourceType;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getCreator() instanceof Player ? PlayerTag.mirrorBukkitPlayer((Player) event.getCreator()) : null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "claim":
                return new GriefPreventionClaimTag(event.getClaim());
            case "source_type":
                return new ElementTag(sourceType);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onClaimCreated(ClaimCreatedEvent event) {
        this.event = event;
        this.sourceType = event.getCreator() instanceof Player ? "player" : "server";
        fire(event);
    }
}
