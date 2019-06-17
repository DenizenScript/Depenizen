package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.extensions.nocheatplus.NoCheatPlusPlayerExtension;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class NoCheatPlusBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(NoCheatPlusPlayerExtension.class, dPlayer.class);
    }
}
