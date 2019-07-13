package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.PlotSquaredPlotTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class PlotSquaredElementProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredElement";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof ElementTag;
    }

    public static PlotSquaredElementProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredElementProperties((ElementTag) object);
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

    private PlotSquaredElementProperties(ElementTag element) {
        this.element = element;
    }

    ElementTag element;

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <ElementTag.as_plotsquared_plot>
        // @returns PlotSquaredPlotTag
        // @description
        // Returns the element as a dPlotSquaredPlot.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("as_plotsquared_plot")) {
            try {
                return PlotSquaredPlotTag.valueOf(element.toString()).getAttribute(attribute.fulfill(1));
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
