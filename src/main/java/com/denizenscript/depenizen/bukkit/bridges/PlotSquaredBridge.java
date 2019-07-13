package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerClaimPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerEntersPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerLeavePlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlotClearScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.plotsquared.PlotSquaredElementProperties;
import com.denizenscript.depenizen.bukkit.properties.plotsquared.PlotSquaredLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.plotsquared.PlotSquaredPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class PlotSquaredBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new PlayerEntersPlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerLeavePlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerClaimPlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlotClearScriptEvent());
        ObjectFetcher.registerWithObjectFetcher(dPlotSquaredPlot.class);
        PropertyParser.registerProperty(PlotSquaredPlayerProperties.class, dPlayer.class);
        PropertyParser.registerProperty(PlotSquaredElementProperties.class, Element.class);
        PropertyParser.registerProperty(PlotSquaredLocationProperties.class, dLocation.class);
    }
}
