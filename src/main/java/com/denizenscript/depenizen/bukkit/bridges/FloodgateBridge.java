package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.containers.floodgate.FormContainer;
import com.denizenscript.depenizen.bukkit.properties.floodgate.FloodgatePlayerProperties;

import java.util.HashMap;
import java.util.Map;

public class FloodgateBridge extends Bridge {
    public static final Map<String, FormContainer> forms = new HashMap<>();
    @Override
    public void init() {
        PropertyParser.registerProperty(FloodgatePlayerProperties.class, PlayerTag.class);
    }
}
