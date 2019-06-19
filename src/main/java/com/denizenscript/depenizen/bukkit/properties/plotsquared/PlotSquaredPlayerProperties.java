package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotSquaredPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredPlayer";
    }

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PlotSquaredPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotSquaredPlayerProperties((dPlayer) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "plotsquared_plots"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlotSquaredPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.plotsquared_plots[<w@world>]>
        // @returns dList(dPlotSquaredPlot)
        // @description
        // Returns a list of plots a player has in a world. Exclude the context to get plots in all worlds.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("plotsquared_plots")) {
            if (attribute.hasContext(1)) {
                dWorld world = dWorld.valueOf(attribute.getContext(1));
                if (world == null) {
                    return null;
                }
                dList plots = new dList();
                for (Plot plays : new PlotAPI().getPlayerPlots(world.getWorld(), player.getPlayerEntity())) {
                    plots.add(new dPlotSquaredPlot(plays).identify());
                }
                return plots.getAttribute(attribute.fulfill(1));
            }
            else {
                dList plots = new dList();
                for (Plot plays : new PlotAPI().getPlayerPlots(player.getPlayerEntity())) {
                    plots.add(new dPlotSquaredPlot(plays).identify());
                }
                return plots.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
