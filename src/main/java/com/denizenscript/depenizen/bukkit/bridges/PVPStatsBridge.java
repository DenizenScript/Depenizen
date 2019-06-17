package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.extensions.pvpstats.PVPStatsPlayerExtension;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PVPStatsBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(PVPStatsPlayerExtension.class, dPlayer.class);
    }
}
