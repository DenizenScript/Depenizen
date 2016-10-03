package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMeLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMeWorldExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMePlayerExtension;

public class PlotMeSupport extends Support {

    public PlotMeSupport() {
        registerProperty(PlotMePlayerExtension.class, dPlayer.class);
        registerProperty(PlotMeLocationExtension.class, dLocation.class);
        registerProperty(PlotMeWorldExtension.class, dWorld.class);
    }
}
