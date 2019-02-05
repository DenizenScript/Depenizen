package com.denizenscript.depenizen.bukkit.extensions.plotsquared;


import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.intellectualcrafters.plot.api.PlotAPI;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.debugging.dB;
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

    public static final String[] handledTags = new String[] {
            "plotsquared_plot"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

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
            org.bukkit.Location loca = new org.bukkit.Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
            try {
                return new dPlotSquaredPlot(new PlotAPI().getPlot(loca)).getAttribute(attribute.fulfill(1));
            }
            catch (Exception e) {
                if (!attribute.hasAlternative()) {
                    dB.echoError(e);
                }
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
