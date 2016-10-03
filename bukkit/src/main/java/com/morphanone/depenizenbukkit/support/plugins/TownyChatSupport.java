package com.morphanone.depenizenbukkit.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.extensions.towny.TownyChatPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class TownyChatSupport extends Support {

    public TownyChatSupport() {
        registerProperty(TownyChatPlayerExtension.class, dPlayer.class);
    }

}
