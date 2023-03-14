package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.plotsquared.PlotSquaredPlotTag;
import com.github.intellectualsites.plotsquared.bukkit.events.PlayerLeavePlotEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeavePlotScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // plotsquared player leaves|exits <'plotsquaredplot'>
    //
    // @Triggers when a player leaves a plot.
    //
    // @Context
    // <context.plot> returns the plot the player left.
    //
    // @Plugin Depenizen, PlotSquared
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerLeavePlotScriptEvent() {
        registerCouldMatcher("plotsquared player leaves|exits <'plotsquaredplot'>");
    }

    public PlayerLeavePlotEvent event;
    public PlayerTag player;
    public PlotSquaredPlotTag plot;

    @Override
    public boolean matches(ScriptPath path) {
        String plotName = path.eventArgLowerAt(3);
        if (!plotName.equals("plotsquaredplot") && !plot.equals(PlotSquaredPlotTag.valueOf(plotName, getTagContext(path)))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotLeave(PlayerLeavePlotEvent event) {
        if (EntityTag.isNPC(event.getPlayer())) {
            return;
        }
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        plot = new PlotSquaredPlotTag(event.getPlot());
        this.event = event;
        fire(event);
    }
}
