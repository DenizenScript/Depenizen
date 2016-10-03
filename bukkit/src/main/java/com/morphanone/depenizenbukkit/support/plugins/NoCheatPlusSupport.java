package com.morphanone.depenizenbukkit.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.extensions.nocheatplus.NoCheatPlusPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class NoCheatPlusSupport extends Support {

    public NoCheatPlusSupport() {
        registerProperty(NoCheatPlusPlayerExtension.class, dPlayer.class);
    }
}
