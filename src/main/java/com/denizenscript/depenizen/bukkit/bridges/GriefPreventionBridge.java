package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.griefprevention.GriefPreventionPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.events.griefprevention.GPClaimEnterEvent;
import com.denizenscript.depenizen.bukkit.properties.griefprevention.GriefPreventionLocationProperties;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class GriefPreventionBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(GriefPreventionClaimTag.class);
        PropertyParser.registerProperty(GriefPreventionPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(GriefPreventionLocationProperties.class, LocationTag.class);
        ScriptEvent.registerScriptEvent(new GPClaimEnterEvent());
    }
}
