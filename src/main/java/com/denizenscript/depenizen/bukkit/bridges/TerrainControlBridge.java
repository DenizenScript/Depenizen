package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.depenizen.bukkit.properties.terraincontrol.TCLocationProperties;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class TerrainControlBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(TCLocationProperties.class, LocationTag.class);
    }
}
