package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dLocation;
import com.denizenscript.depenizen.bukkit.extensions.terraincontrol.TCLocationExtension;

public class TerrainControlSupport extends Support {

    public TerrainControlSupport() {
        registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
