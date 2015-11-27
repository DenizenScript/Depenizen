package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dLocation;
import net.gnomeffinway.depenizen.extensions.terraincontrol.TCLocationExtension;
import net.gnomeffinway.depenizen.support.Support;

public class TerrainControlSupport extends Support {

    public TerrainControlSupport() {
        registerProperty(TCLocationExtension.class, dLocation.class);
    }
}
