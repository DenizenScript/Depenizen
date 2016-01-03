package net.gnomeffinway.depenizen.extensions.plotme;

import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;

public class PlotMeWorldExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dWorld;
    }

    public static PlotMeWorldExtension getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new PlotMeWorldExtension((dWorld) loc);
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private PlotMeWorldExtension(dWorld loc) {
        location = loc;
    }

    dWorld location;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <w@world.is_plot_world>
        // @returns Element(Boolean)
        // @description
        // Returns whether the world is a plot world.
        // @plugin Depenizen, PlotMe
        // -->
        if (attribute.startsWith("is_plot_world")) {
            return new Element(PlotMeCoreManager.getInstance().isPlotWorld(new BukkitWorld(location.getWorld()))).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
