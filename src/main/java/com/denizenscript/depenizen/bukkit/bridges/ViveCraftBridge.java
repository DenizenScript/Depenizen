package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.vivecraft.ViveMirrorCommand;
import com.denizenscript.depenizen.bukkit.objects.vivecraft.ViveCraftPlayerTag;
import com.denizenscript.depenizen.bukkit.properties.vivecraft.ViveCraftPlayerProperties;
import org.bukkit.entity.Player;
import org.vivecraft.VSE;
import org.vivecraft.VivePlayer;

public class ViveCraftBridge extends Bridge {

    @Override
    public void init() {
        ViveCraftPlayerProperties.register();
        ObjectFetcher.registerWithObjectFetcher(ViveCraftPlayerTag.class, ViveCraftPlayerTag.tagProcessor);
        DenizenCore.commandRegistry.registerCommand(ViveMirrorCommand.class);
    }

    public static boolean isViveCraftPlayer(Player player) {
        return VSE.isVive(player);
    }

    public static VivePlayer getViveCraftPlayer(Player player) {
        return VSE.vivePlayers.get(player.getUniqueId());
    }
}
