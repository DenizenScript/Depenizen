package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.properties.worldedit.WorldEditPlayerProperties;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class WorldEditBridge extends Bridge {

    public static WorldEditBridge instance;

    @Override
    public void init() {
        instance = this;
        PropertyParser.registerProperty(WorldEditPlayerProperties.class, dPlayer.class);
    }
}
