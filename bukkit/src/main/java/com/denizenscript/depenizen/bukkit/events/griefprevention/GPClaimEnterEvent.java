package com.denizenscript.depenizen.bukkit.events.griefprevention;

import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaim;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GPClaimEnterEvent extends ScriptEvent implements Listener {

    // TODO: in area?
    // <--[event]
    // @Events
    // gp player enters <gpclaim>
    // gp player exits <gpclaim>
    // gp player enters gpclaim
    // gp player exits gpclaim
    //
    // @Regex ^on gp player (enters|exits) [^\s]+$
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
    // @Plugin DepenizenBukkit, GriefPrevention
    // -->

    public GPClaimEnterEvent() {
        instance = this;
    }

    public static GPClaimEnterEvent instance;

    public dLocation from;
    public dLocation to;
    public GriefPreventionClaim new_claim;
    public GriefPreventionClaim old_claim;
    public PlayerMoveEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("gp player enters")
                || lower.startsWith("gp player exits");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String claim_test = lower.substring(lower.lastIndexOf(' ') + 1);
        String direction = lower.substring("gp player ".length(), lower.lastIndexOf(' ')).trim();

        return (claim_test.equals("gpclaim") && ((direction.equals("enters") && new_claim != null) || (direction.equals("exits") && old_claim != null)))
                || (direction.equals("enters") && new_claim != null && claim_test.equals(CoreUtilities.toLowerCase(new_claim.simple())))
                || (direction.equals("exits") && old_claim != null && claim_test.equals(CoreUtilities.toLowerCase(old_claim.simple())));
    }

    @Override
    public String getName() {
        return "GPClaimEnter";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        // TODO: Store the player?
        return new BukkitScriptEntryData(event != null ? dEntity.getPlayerFrom(event.getPlayer()) : null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("to")) {
            return to;
        }
        else if (name.equals("from")) {
            return from;
        }
        else if (name.equals("old_claim")) {
            return old_claim;
        }
        else if (name.equals("new_claim")) {
            return new_claim;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerEntersExitsGPClaim(PlayerMoveEvent event) {
        if (event.getTo().getBlock().getLocation().equals(event.getFrom().getBlock().getLocation())) {
            return;
        }
        from = new dLocation(event.getFrom());
        to = new dLocation(event.getTo());
        Claim fclaim = GriefPrevention.instance.dataStore.getClaimAt(from, false, null);
        Claim tclaim = GriefPrevention.instance.dataStore.getClaimAt(to, false, null);
        if (tclaim == fclaim) {
            return;
        }
        new_claim = tclaim == null ? null : new GriefPreventionClaim(tclaim);
        old_claim = fclaim == null ? null : new GriefPreventionClaim(fclaim);
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
