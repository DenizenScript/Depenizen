package com.denizenscript.depenizen.bukkit.support.plugins;


import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerEntersPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerLeavePlotScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredElementExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;

public class PlotSquaredSupport extends Support {

    public PlotSquaredSupport() {
        registerScriptEvents(new PlayerEntersPlotScriptEvent());
        registerScriptEvents(new PlayerLeavePlotScriptEvent());
        registerObjects(dPlotSquaredPlot.class);
        registerProperty(PlotSquaredPlayerExtension.class, dPlayer.class);
        registerProperty(PlotSquaredElementExtension.class, Element.class);
        registerProperty(PlotSquaredLocationExtension.class, dLocation.class);
    }
}
