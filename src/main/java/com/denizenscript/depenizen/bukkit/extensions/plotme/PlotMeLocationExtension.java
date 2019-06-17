package com.denizenscript.depenizen.bukkit.extensions.plotme;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotMeLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static PlotMeLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlotMeLocationExtension((dLocation) object);
        }
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "plot"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlotMeLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.plot>
        // @returns dPlot
        // @description
        // Returns the plot contained by this location.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("plot")) {
            Location l = new Location(new BukkitWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
            return new dPlot(PlotMeCoreManager.getInstance().getPlot(l)).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
