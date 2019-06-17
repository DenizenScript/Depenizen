package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerClaimPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerEntersPlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlayerLeavePlotScriptEvent;
import com.denizenscript.depenizen.bukkit.events.plotsquared.PlotClearScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredElementExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredLocationExtension;
import com.denizenscript.depenizen.bukkit.extensions.plotsquared.PlotSquaredPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;

public class PlotSquaredBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(new PlayerEntersPlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerLeavePlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerClaimPlotScriptEvent());
        ScriptEvent.registerScriptEvent(new PlotClearScriptEvent());
        ObjectFetcher.registerWithObjectFetcher(dPlotSquaredPlot.class);
        PropertyParser.registerProperty(PlotSquaredPlayerExtension.class, dPlayer.class);
        PropertyParser.registerProperty(PlotSquaredElementExtension.class, Element.class);
        PropertyParser.registerProperty(PlotSquaredLocationExtension.class, dLocation.class);
    }
}
