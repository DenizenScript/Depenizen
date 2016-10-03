package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.towny.TownyChatPlayerExtension;

public class TownyChatSupport extends Support {

    public TownyChatSupport() {
        registerProperty(TownyChatPlayerExtension.class, dPlayer.class);
    }

}
