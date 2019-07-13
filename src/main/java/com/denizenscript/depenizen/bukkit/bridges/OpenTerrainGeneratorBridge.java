package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.terraincontrol.TCLocationProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class OpenTerrainGeneratorBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(TCLocationProperties.class, dLocation.class);
    }
}
