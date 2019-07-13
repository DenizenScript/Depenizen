package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.properties.nocheatplus.NoCheatPlusPlayerProperties;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class NoCheatPlusBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(NoCheatPlusPlayerProperties.class, PlayerTag.class);
    }
}
