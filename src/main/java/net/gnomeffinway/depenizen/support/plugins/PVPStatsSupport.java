package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.extensions.pvpstats.PVPStatsPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class PVPStatsSupport extends Support {

    public PVPStatsSupport() {
        registerProperty(PVPStatsPlayerExtension.class, dPlayer.class);
    }
}
