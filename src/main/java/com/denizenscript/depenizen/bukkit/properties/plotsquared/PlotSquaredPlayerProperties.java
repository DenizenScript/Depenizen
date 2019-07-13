package com.denizenscript.depenizen.bukkit.properties.plotsquared;

import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizen.objects.dWorld;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class PlotSquaredPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotSquaredPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dPlayer;
    }

    public static PlotSquaredPlayerProperties getFrom(ObjectTag object) {
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
        // @returns ListTag(dPlotSquaredPlot)
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
                ListTag plots = new ListTag();
                for (Plot plays : PlotPlayer.wrap(player.getPlayerEntity()).getPlots(world.getName())) {
                    plots.add(new dPlotSquaredPlot(plays).identify());
                }
                return plots.getAttribute(attribute.fulfill(1));
            }
            else {
                ListTag plots = new ListTag();
                for (Plot plays : PlotPlayer.wrap(player.getPlayerEntity()).getPlots()) {
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
