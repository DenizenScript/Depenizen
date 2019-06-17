package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.vampire.VampirePlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class VampireBridge extends Bridge {
    @Override
    public void init() {
        PropertyParser.registerProperty(VampirePlayerExtension.class, dPlayer.class);
    }
}
