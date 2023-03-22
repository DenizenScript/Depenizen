package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.PlotSquaredPlotTag;
import com.github.intellectualsites.plotsquared.api.PlotAPI;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class PlotSquaredLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredLocation";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static PlotSquaredLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "plotsquared_plot"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public PlotSquaredLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

    public Location getPlotSquaredLocation() {
        return new Location(location.getWorldName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <LocationTag.plotsquared_plot>
        // @returns PlotSquaredPlotTag
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot contained by this location.
        // -->
        if (attribute.startsWith("plotsquared_plot")) {
            try {
                return new PlotSquaredPlotTag(new PlotAPI().getPlotSquared().getPlotAreaAbs(getPlotSquaredLocation())
                        .getPlot(getPlotSquaredLocation())).getObjectAttribute(attribute.fulfill(1));
            }
            catch (Exception e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError(e);
                }
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
