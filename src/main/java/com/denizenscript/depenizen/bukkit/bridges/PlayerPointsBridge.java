package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.playerpoints.PlayerPointsCommand;
import com.denizenscript.depenizen.bukkit.properties.playerpoints.PlayerPointsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class PlayerPointsBridge extends Bridge {

    public static PlayerPointsBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(PlayerPointsCommand.class,
                "PLAYERPOINTS", "playerpoints [set/give/take] (amount:<amount>) (target:<player>)", 2);
        PropertyParser.registerProperty(PlayerPointsPlayerProperties.class, PlayerTag.class);
    }
}
