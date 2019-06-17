package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.playerpoints.PlayerPointsCommand;
import com.denizenscript.depenizen.bukkit.properties.playerpoints.PlayerPointsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PlayerPointsBridge extends Bridge {

    public static PlayerPointsBridge instance;

    @Override
    public void init() {
        instance = this;
        new PlayerPointsCommand().activate().as("playerpoints").withOptions("See Documentation.", 2);
        PropertyParser.registerProperty(PlayerPointsPlayerProperties.class, dPlayer.class);
    }
}
