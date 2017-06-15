package com.denizenscript.depenizen.bukkit.extensions.plotsquared;


import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.intellectualcrafters.plot.api.PlotAPI;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;


public class PlotSquaredLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static PlotSquaredLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredLocationExtension((dLocation) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    private PlotSquaredLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location;

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <l@location.plotsquared_plot>
        // @returns dPlotSquaredPlot
        // @description
        // Returns the plot contained by this location.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("plotsquared_plot")) {
            org.bukkit.Location loca = new org.bukkit.Location(location.getWorld(),location.getBlockX(),location.getBlockY(),location.getBlockZ(),location.getYaw(),location.getPitch());

            //return new dPlotSquaredPlot(new dPlotSquaredPlot(Plot.getPlot(location1)));
            return new dPlotSquaredPlot(new PlotAPI().getPlot(loca)).getAttribute(attribute.fulfill(1));
            //return new dPlotSquaredPlot(plot.getPlot(loca));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
