package com.denizenscript.depenizen.bukkit.events.towny;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerClaimsPlotScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // towny player claims plot
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player tries to claim a new Towny plot or outpost.
    //
    // @Context
    // <context.is_outpost> Returns whether the new plot is an outpost (not connected to the main town).
    // <context.is_new_town> Returns whether this is a new town. New towns may not have certain tags available.
    // <context.town> Returns a TownTag of the town.
    // <context.cuboid> Returns the cuboid that will be claimed by the town.
    //
    // @Determine
    // "CANCEL_MESSAGE:<ElementTag>" to set the message Towny sends when cancelled.
    //
    // @Plugin Depenizen, Towny
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerClaimsPlotScriptEvent() {
        registerCouldMatcher("towny player claims plot");
    }

    public TownPreClaimEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, event.getPlayer().getLocation())) {
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
            case "is_outpost":
                return new ElementTag(event.isOutpost());
            case "is_new_town":
                return new ElementTag(event.isHomeBlock());
            case "town":
                return new TownTag(event.getTown());
            case "cuboid":
                return TownTag.getCuboid(event.getPlayer().getWorld(), event.getTownBlock().getX(), event.getTownBlock().getZ());
        }
        return super.getContext(name);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String determination = determinationObj.toString();
            String lower = CoreUtilities.toLowerCase(determination);
            if (lower.startsWith("cancel_message:")) {
                event.setCancelMessage(determination.substring("cancel_message:".length()));
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @EventHandler
    public void onTownyPlayerClaimsPlot(TownPreClaimEvent event) {
        this.event = event;
        fire(event);
    }
}
