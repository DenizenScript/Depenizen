package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.coordinateoffset.CoordinateOffsetPlayerExtensions;

public class CoordinateOffsetBridge extends Bridge {

    @Override
    public void init() {
        CoordinateOffsetPlayerExtensions.register();
    }
}
