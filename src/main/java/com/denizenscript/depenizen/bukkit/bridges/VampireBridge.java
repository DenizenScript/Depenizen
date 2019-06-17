package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.vampire.VampirePlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class VampireBridge extends Bridge {
    @Override
    public void init() {
        PropertyParser.registerProperty(VampirePlayerProperties.class, dPlayer.class);
    }
}
