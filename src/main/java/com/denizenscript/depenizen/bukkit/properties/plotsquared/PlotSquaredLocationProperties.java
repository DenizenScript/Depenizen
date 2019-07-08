package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import com.github.intellectualsites.plotsquared.api.PlotAPI;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotSquaredLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredLocation";
    }

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static PlotSquaredLocationProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredLocationProperties((dLocation) object);
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

    private PlotSquaredLocationProperties(dLocation location) {
        this.location = location;
    }

    dLocation location;

    public Location getPlotSquaredLocation() {
        return new Location(location.getWorldName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <l@location.plotsquared_plot>
        // @returns dPlotSquaredPlot
        // @description
        // Returns the plot contained by this location.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("plotsquared_plot")) {
            try {
                return new dPlotSquaredPlot(new PlotAPI().getPlotSquared().getPlotAreaAbs(getPlotSquaredLocation())
                        .getPlot(getPlotSquaredLocation())).getAttribute(attribute.fulfill(1));
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
    }
}
