package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.towny.TownyChatPlayerExtension;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class TownyChatBridge extends Bridge {

    public static TownyChatBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(TownyChatPlayerExtension.class, dPlayer.class);
    }
}
