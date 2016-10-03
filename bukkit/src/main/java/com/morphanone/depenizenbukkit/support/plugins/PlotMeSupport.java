package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.extensions.plotme.PlotMeWorldExtension;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import com.morphanone.depenizenbukkit.extensions.plotme.PlotMeLocationExtension;
import com.morphanone.depenizenbukkit.extensions.plotme.PlotMePlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class PlotMeSupport extends Support {

    public PlotMeSupport() {
        registerProperty(PlotMePlayerExtension.class, dPlayer.class);
        registerProperty(PlotMeLocationExtension.class, dLocation.class);
        registerProperty(PlotMeWorldExtension.class, dWorld.class);
    }
}
