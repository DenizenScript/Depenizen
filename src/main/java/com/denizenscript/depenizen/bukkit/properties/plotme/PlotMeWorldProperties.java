package com.denizenscript.depenizen.bukkit.properties.plotme;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class PlotMeWorldProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlotMeWorld";
    }

    public static boolean describes(dObject world) {
        return world instanceof dWorld;
    }

    public static PlotMeWorldProperties getFrom(dObject world) {
        if (!describes(world)) {
            return null;
        }
        else {
            return new PlotMeWorldProperties((dWorld) world);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "is_plot_world"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlotMeWorldProperties(dWorld world) {
        this.world = world;
    }

    dWorld world;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <w@world.is_plot_world>
        // @returns Element(Boolean)
        // @description
        // Returns whether the world is a plot world.
        // @Plugin Depenizen, PlotMe
        // -->
        if (attribute.startsWith("is_plot_world")) {
            return new Element(PlotMeCoreManager.getInstance().isPlotWorld(new BukkitWorld(world.getWorld()))).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
