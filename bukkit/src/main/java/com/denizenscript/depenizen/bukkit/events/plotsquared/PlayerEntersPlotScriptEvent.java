package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// plotsquared player enters plotsquaredplot
// plotsquared player enters <dplotsquaredplot>
//
// @Regex ^on plotsquared player [^\s]+ level changes( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable false
//
// @Triggers when a player enters a plot.
//
// @Context
// <context.plot> returns the plot the player entered.
//
// @Plugin DepenizenBukkit, PlotSquared
//
// -->

public class PlayerEntersPlotScriptEvent extends BukkitScriptEvent implements Listener {

    public PlayerEntersPlotScriptEvent() {
        instance = this;
    }

    public static PlayerEntersPlotScriptEvent instance;
    public PlayerEnterPlotEvent event;
    public dPlayer player;
    public dPlotSquaredPlot plot;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("plotsquared player enters");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String plotName = path.eventArgLowerAt(3);
        if (plotName.equals("plotsquaredplot")) {
            return true;
        }
        dPlotSquaredPlot dplot = dPlotSquaredPlot.valueOf(plotName);
        return dplot != null && dplot.equals(plot);
    }

    @Override
    public String getName() {
        return "PlayerEnterPlotEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotEnter(PlayerEnterPlotEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        plot = new dPlotSquaredPlot(event.getPlot());
        this.event = event;
        fire();
    }
}
