package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.plotsquared.dPlotSquaredPlot;
import com.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerClaimPlotScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // plotsquared player claims plotsquaredplot
    // plotsquared player claims <dplotsquaredplot>
    //
    // @Regex ^on plotsquared player [^\s]+ level changes( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
    //
    // @Cancellable true
    //
    // @Triggers when a player claims a plot.
    //
    // @Context
    // <context.plot> returns the plot the player claimed.
    // <context.auto> returns true if the plot was claimed automatic.
    //
    // @Plugin Depenizen, PlotSquared
    //
    // -->

    public PlayerClaimPlotScriptEvent() {
        instance = this;
    }

    public static PlayerClaimPlotScriptEvent instance;
    public PlayerClaimPlotEvent event;
    public dPlayer player;
    public dPlotSquaredPlot plot;
    public Element auto;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("plotsquared player claims");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String plotName = path.eventArgLowerAt(3);
        if (plotName.equals("plotsquaredplot")) {
            return true;
        }
        dPlotSquaredPlot dplot = dPlotSquaredPlot.valueOf(plotName);
        return dplot != null && dplot.equals(plot);
    }

    @Override
    public String getName() {
        return "PlayerClaimPlotEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        else if (name.equals("auto")) {
            return auto;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotEnter(PlayerClaimPlotEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        plot = new dPlotSquaredPlot(event.getPlot());
        auto = new Element(event.wasAuto());
        this.event = event;
        fire(event);
    }
}
