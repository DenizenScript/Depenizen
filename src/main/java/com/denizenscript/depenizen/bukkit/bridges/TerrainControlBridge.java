package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import com.denizenscript.depenizen.bukkit.extensions.terraincontrol.TCLocationExtension;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class TerrainControlBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
