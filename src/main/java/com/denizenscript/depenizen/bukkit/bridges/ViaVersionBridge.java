package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.viaversion.ViaVersionPlayerProperties;

public class ViaVersionBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(ViaVersionPlayerProperties.class, PlayerTag.class);
    }
}
