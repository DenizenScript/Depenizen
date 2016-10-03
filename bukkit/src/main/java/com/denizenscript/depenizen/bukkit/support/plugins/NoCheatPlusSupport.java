package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.nocheatplus.NoCheatPlusPlayerExtension;

public class NoCheatPlusSupport extends Support {

    public NoCheatPlusSupport() {
        registerProperty(NoCheatPlusPlayerExtension.class, dPlayer.class);
    }
}
