package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.properties.towny.TownyChatPlayerProperties;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class TownyChatBridge extends Bridge {

    public static TownyChatBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(TownyChatPlayerProperties.class, PlayerTag.class);
    }
}
