package com.morphanone.depenizenbukkit.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import com.morphanone.depenizenbukkit.extensions.terraincontrol.TCLocationExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class TerrainControlSupport extends Support {

    public TerrainControlSupport() {
        registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
