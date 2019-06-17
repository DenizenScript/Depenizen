package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.plotme.PlotMeLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.plotme.PlotMeWorldProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import com.denizenscript.depenizen.bukkit.properties.plotme.PlotMePlayerProperties;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PlotMeBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(PlotMePlayerProperties.class, dPlayer.class);
        PropertyParser.registerProperty(PlotMeLocationProperties.class, dLocation.class);
        PropertyParser.registerProperty(PlotMeWorldProperties.class, dWorld.class);
    }
}
