package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.terraincontrol.TCLocationExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dLocation;

public class OpenTerrainGeneratorSupport extends Support {

    public OpenTerrainGeneratorSupport() {
        registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
