package com.morphanone.depenizenbukkit.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.extensions.pvpstats.PVPStatsPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

public class PVPStatsSupport extends Support {

    public PVPStatsSupport() {
        registerProperty(PVPStatsPlayerExtension.class, dPlayer.class);
    }
}
