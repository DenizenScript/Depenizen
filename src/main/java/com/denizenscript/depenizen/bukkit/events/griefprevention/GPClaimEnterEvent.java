package com.denizenscript.depenizen.bukkit.events.griefprevention;

import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GPClaimEnterEvent extends BukkitScriptEvent implements Listener {

    // TODO: in area?
    // <--[event]
    // @Events
    // gp player enters|exits <'gpclaim'>
    //
    // @Warning Cancelling this event will fire a similar event immediately after.
    //
    // @Cancellable true
    //
    // @Triggers when a player enters or exits a Grief Prevention claim.
    //
    // @Context
    // <context.from> returns the block location moved from.
    // <context.to> returns the block location moved to.
    // <context.new_claim> returns the gpclaim being entered.
    // <context.old_claim> returns the gpclaim being exited.
    //
    // @Plugin Depenizen, GriefPrevention
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public GPClaimEnterEvent() {
        registerCouldMatcher("gp player enters|exits <'gpclaim'>");
    }

    public static GPClaimEnterEvent instance;

    public LocationTag from;
    public LocationTag to;
    public GriefPreventionClaimTag new_claim;
    public GriefPreventionClaimTag old_claim;
    public PlayerMoveEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        String direction = path.eventArgAt(2);
        String claim_test = path.eventArgLowerAt(3);

        // TODO: Wtf
        if (direction.equals("enters") && new_claim != null) {
            if (!(claim_test.equals("gpclaim") || claim_test.equals(CoreUtilities.toLowerCase(new_claim.simple())))) {
                return false;
            }
            return super.matches(path);
        }
        else if (direction.equals("exits") && old_claim != null) {
            if (!(claim_test.equals("gpclaim") || claim_test.equals(CoreUtilities.toLowerCase(old_claim.simple())))) {
                return false;
            }
            return super.matches(path);
        }
        return false;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        // TODO: Store the player?
        return new BukkitScriptEntryData(event != null ? new PlayerTag(event.getPlayer()) : null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "to" -> to;
            case "from" -> from;
            case "old_claim" -> old_claim;
            case "new_claim" -> new_claim;
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerEntersExitsGPClaim(PlayerMoveEvent event) {
        if (LocationTag.isSameBlock(event.getFrom(), event.getTo())) {
            return;
        }
        from = new LocationTag(event.getFrom());
        to = new LocationTag(event.getTo());
        Claim fclaim = GriefPrevention.instance.dataStore.getClaimAt(from, false, null);
        Claim tclaim = GriefPrevention.instance.dataStore.getClaimAt(to, false, null);
        if (tclaim == fclaim) {
            return;
        }
        new_claim = tclaim == null ? null : new GriefPreventionClaimTag(tclaim);
        old_claim = fclaim == null ? null : new GriefPreventionClaimTag(fclaim);
        this.event = event;
        fire(event);
    }
}
