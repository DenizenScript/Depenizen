package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.residence.PlayerExitsResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidenceLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidencePlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.residence.PlayerEntersResidenceScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.residence.dResidence;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class ResidenceBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(dResidence.class);
        PropertyParser.registerProperty(ResidencePlayerProperties.class, dPlayer.class);
        PropertyParser.registerProperty(ResidenceLocationProperties.class, dLocation.class);
        ScriptEvent.registerScriptEvent(new PlayerEntersResidenceScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerExitsResidenceScriptEvent());
    }
}
