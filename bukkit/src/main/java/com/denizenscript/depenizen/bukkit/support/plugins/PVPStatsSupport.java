package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.pvpstats.PVPStatsPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;

public class PVPStatsSupport extends Support {

    public PVPStatsSupport() {
        registerProperty(PVPStatsPlayerExtension.class, dPlayer.class);
    }
}
