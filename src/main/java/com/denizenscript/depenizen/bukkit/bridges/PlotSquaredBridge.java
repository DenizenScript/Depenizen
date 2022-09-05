package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerClaimPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerEntersPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerLeavePlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlotClearScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.plotsquared.PlotSquaredLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.plotsquared.PlotSquaredPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.PlotSquaredPlotTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class PlotSquaredBridge extends Bridge {

    @Override
    public void init() {
        // TODO: Remove PlotSquared? Newer versions are premium only, but licensing implies possibility of free updates?
        if (!plugin.getDescription().getVersion().startsWith("4.")) {
            Debug.log("Ignoring PlotSquared bridge, unsupported version.");
            return;
        }
        ScriptEvent.registerScriptEvent(PlayerEntersPlotScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerLeavePlotScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerClaimPlotScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlotClearScriptEvent.class);
        ObjectFetcher.registerWithObjectFetcher(PlotSquaredPlotTag.class);
        PropertyParser.registerProperty(PlotSquaredPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(PlotSquaredLocationProperties.class, LocationTag.class);
    }
}
