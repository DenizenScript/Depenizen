package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.pvpstats.PVPStatsPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class PVPStatsBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(PVPStatsPlayerProperties.class, dPlayer.class);
    }
}
