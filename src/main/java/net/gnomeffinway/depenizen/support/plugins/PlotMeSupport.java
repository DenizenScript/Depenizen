package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.gnomeffinway.depenizen.extensions.plotme.PlotMeLocationExtension;
import net.gnomeffinway.depenizen.extensions.plotme.PlotMePlayerExtension;
import net.gnomeffinway.depenizen.extensions.plotme.PlotMeWorldExtension;
import net.gnomeffinway.depenizen.support.Support;

public class PlotMeSupport extends Support {

    public PlotMeSupport() {
        registerProperty(PlotMePlayerExtension.class, dPlayer.class);
        registerProperty(PlotMeLocationExtension.class, dLocation.class);
        registerProperty(PlotMeWorldExtension.class, dWorld.class);
    }
}
