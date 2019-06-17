package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMeLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMeWorldExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import com.denizenscript.depenizen.bukkit.extensions.plotme.PlotMePlayerExtension;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PlotMeBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(PlotMePlayerExtension.class, dPlayer.class);
        PropertyParser.registerProperty(PlotMeLocationExtension.class, dLocation.class);
        PropertyParser.registerProperty(PlotMeWorldExtension.class, dWorld.class);
    }
}
