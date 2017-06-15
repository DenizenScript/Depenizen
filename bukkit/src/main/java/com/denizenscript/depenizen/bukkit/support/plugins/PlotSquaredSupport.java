package com.denizenscript.depenizen.bukkit.support.plugins;


import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;

public class PlotSquaredSupport extends Support {

    public PlotSquaredSupport() {
        registerObjects(dPlotSquaredPlot.class);
        registerProperty(PlotSquaredPlayerExtension.class, dPlayer.class);
        registerProperty(PlotSquaredLocationExtension.class, dLocation.class);
    }
}
