package net.gnomeffinway.depenizen.extensions.plotme;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dPlot;

public class PlotMeLocationExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dLocation;
    }

    public static PlotMeLocationExtension getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new PlotMeLocationExtension((dLocation) loc);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private PlotMeLocationExtension(dLocation loc) {
        location = loc;
    }

    dLocation location;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.plot>
        // @returns dPlot
        // @description
        // Returns the plot contained by this location.
        // @plugin Depenizen, PlotMe
        // -->
        if (attribute.startsWith("plot")) {
            Location l = new Location(new BukkitWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
            return new dPlot(PlotMeCoreManager.getInstance().getPlot(l)).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
