package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.commands.playerpoints.PlayerPointsCommand;
import com.denizenscript.depenizen.bukkit.extensions.playerpoints.PlayerPointsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import org.black_ixx.playerpoints.PlayerPoints;

public class PlayerPointsSupport extends Support {
    PlayerPoints plugin;

    public PlayerPointsSupport() {
        new PlayerPointsCommand().activate().as("playerpoints").withOptions("See Documentation.", 2);
        registerProperty(PlayerPointsPlayerExtension.class, dPlayer.class);
        plugin = Support.getPlugin(PlayerPointsSupport.class);
    }
}
