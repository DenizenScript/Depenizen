package com.denizenscript.depenizen.bukkit.events.griefprevention;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.events.ClaimChangeEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExtendEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GPClaimChangedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // gp claim changed
    //
    // @Cancellable true
    //
    // @Triggers when a Grief Prevention claim is modified.
    //
    // @Context
    // <context.old_claim> returns the GriefPreventionClaimTag of the old claim.
    // <context.new_claim> returns the GriefPreventionClaimTag of the new claim.
    // <context.source_type> returns the source of the change. Can be: PLAYER, SERVER, or AUTO_DEEPEN.
    //
    // @Plugin Depenizen, GriefPrevention
    //
    // @Player when source_type is player.
    //
    // @Group Depenizen
    //
    // -->

    public GPClaimChangedScriptEvent() {
        registerCouldMatcher("gp claim changed");
    }

    public ClaimChangeEvent event;
    public Player player;
    public String sourceType;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player != null ? PlayerTag.mirrorBukkitPlayer(player) : null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "old_claim":
                return new GriefPreventionClaimTag(event.getFrom());
            case "new_claim":
                return new GriefPreventionClaimTag(event.getTo());
            case "source_type":
                return new ElementTag(sourceType);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onClaimChanged(ClaimChangeEvent event) {
        this.event = event;
        this.player = null;
        this.sourceType = "server";
        if (event instanceof ClaimExtendEvent) {
            this.sourceType = "auto_deepen";
        }
        else if (event instanceof ClaimResizeEvent && ((ClaimResizeEvent) event).getModifier() instanceof Player) {
            this.player = (Player) ((ClaimResizeEvent) event).getModifier();
            this.sourceType = "player";
        }
        fire(event);
    }
}
