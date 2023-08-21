package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.viaversion.ViaVersionPlayerExtensions;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;

public class ViaVersionBridge extends Bridge {

    public static ViaAPI viaVersionInstance;

    @Override
    public void init() {
        viaVersionInstance = Via.getAPI();
        ViaVersionPlayerExtensions.register();
    }
}
