package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.betonquest.BetonQuestPlayerProperties;

public class BetonQuestBridge extends Bridge {

    @Override
    public void init() {
        PropertyParser.registerProperty(BetonQuestPlayerProperties.class, PlayerTag.class);
    }
}
