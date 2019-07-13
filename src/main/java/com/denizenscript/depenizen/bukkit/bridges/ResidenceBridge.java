package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.residence.PlayerExitsResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidenceLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidencePlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.residence.PlayerEntersResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class ResidenceBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(dResidence.class);
        PropertyParser.registerProperty(ResidencePlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(ResidenceLocationProperties.class, LocationTag.class);
        ScriptEvent.registerScriptEvent(new PlayerEntersResidenceScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerExitsResidenceScriptEvent());
    }
}
