package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.terraincontrol.TCLocationExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class OpenTerrainGeneratorBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
