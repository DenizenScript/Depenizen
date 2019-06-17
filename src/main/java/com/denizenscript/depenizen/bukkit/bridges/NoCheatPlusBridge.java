package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.properties.nocheatplus.NoCheatPlusPlayerProperties;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class NoCheatPlusBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(NoCheatPlusPlayerProperties.class, dPlayer.class);
    }
}
