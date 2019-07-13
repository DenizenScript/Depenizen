package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.vampire.VampirePlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class VampireBridge extends Bridge {
    @Override
    public void init() {
        PropertyParser.registerProperty(VampirePlayerProperties.class, PlayerTag.class);
    }
}
