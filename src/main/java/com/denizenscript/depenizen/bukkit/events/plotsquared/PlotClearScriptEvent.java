package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.plotsquared.PlotSquaredPlotTag;
import com.github.intellectualsites.plotsquared.bukkit.events.PlotClearEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlotClearScriptEvent extends BukkitScriptEvent implements Listener {

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
    // @Plugin Depenizen, PlotSquared
    //
    // -->

    public PlotClearScriptEvent() {
        instance = this;
    }

    public static PlotClearScriptEvent instance;
    public PlotClearEvent event;
    public PlotSquaredPlotTag plot;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("plotsquared plot clear") || path.eventLower.startsWith("plotsquared plot clears");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String plotName = path.eventArgLowerAt(3);
        if (!plotName.equals("plotsquaredplot") && !plot.equals(PlotSquaredPlotTag.valueOf(plotName))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "PlotClearEvent";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotClear(PlotClearEvent event) {
        plot = new PlotSquaredPlotTag(event.getPlot());
        this.event = event;
        fire(event);
    }
}
