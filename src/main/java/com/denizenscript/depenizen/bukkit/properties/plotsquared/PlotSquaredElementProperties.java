package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotSquaredElementProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredElement";
    }

    public static boolean describes(dObject object) {
        return object instanceof Element;
    }

    public static PlotSquaredElementProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredElementProperties((Element) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "as_plotsquared_plot"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlotSquaredElementProperties(Element element) {
        this.element = element;
    }

    Element element;

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <el@element.as_plotsquared_plot>
        // @returns dPlotSquaredPlot
        // @description
        // Returns the element as a dPlotSquaredPlot.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("as_plotsquared_plot")) {
            try {
                return dPlotSquaredPlot.valueOf(element.toString()).getAttribute(attribute.fulfill(1));
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
