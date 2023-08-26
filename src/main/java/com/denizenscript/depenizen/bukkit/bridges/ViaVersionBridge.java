package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.viaversion.ViaVersionPlayerExtensions;

public class ViaVersionBridge extends Bridge {

    @Override
    public void init() {
        ViaVersionPlayerExtensions.register();
    }
}
