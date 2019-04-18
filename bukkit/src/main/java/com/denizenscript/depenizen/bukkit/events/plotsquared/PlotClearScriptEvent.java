package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.plotsquared.bukkit.events.PlotClearEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// plotsquared plot clear plotsquaredplot
// plotsquared plot clears plotsquaredplot
// plotsquared plot clear <dplotsquaredplot>
// plotsquared plot clears <dplotsquaredplot>
//
// @Regex ^on plotsquared plot [^\s]+ clears( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
//
// @Cancellable true
//
// @Triggers when a plot is cleared.
//
// @Context
// <context.plot> returns the plot that is cleared.
//
// @Plugin DepenizenBukkit, PlotSquared
//
// -->

public class PlotClearScriptEvent extends BukkitScriptEvent implements Listener {

    public PlotClearScriptEvent() {
        instance = this;
    }

    public static PlotClearScriptEvent instance;
    public PlotClearEvent event;
    public dPlotSquaredPlot plot;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("plotsquared plot clear") || lower.startsWith("plotsquared plot clears");
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
        return "PlotClearEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotClear(PlotClearEvent event) {
        plot = new dPlotSquaredPlot(event.getPlot());

        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
