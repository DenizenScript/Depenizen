package com.denizenscript.depenizen.bukkit.extensions.plotsquared;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotSquaredElementExtension extends dObjectExtension {
    public static boolean describes(dObject object) {
        return object instanceof Element;
    }

    public static PlotSquaredElementExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredElementExtension((Element) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    private PlotSquaredElementExtension(Element element) {
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
        // @Plugin DepenizenBukkit, PlotSquared
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
        Element value = mechanism.getValue();
    }
}
