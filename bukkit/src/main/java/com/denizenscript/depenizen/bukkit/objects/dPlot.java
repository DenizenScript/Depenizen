package com.denizenscript.depenizen.bukkit.objects;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class dPlot implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static dPlot valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("plot")
    public static dPlot valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match town name

        string = string.replace("plot@", "");
        try {
            List<String> split = CoreUtilities.split(string, ',');
            return new dPlot(PlotMeCoreManager.getInstance().getPlotById(
                    new PlotId(aH.getIntegerFrom(split.get(0)), aH.getIntegerFrom(split.get(1))),
                    new BukkitWorld(dWorld.valueOf(split.get(3)).getWorld())));
        }
        catch (Throwable e) {
            return null;
        }
    }

    public static boolean matches(String arg) {
        return arg.startsWith("plot@");
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Plot plot = null;

    public dPlot(Plot pl) {
        plot = pl;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Plot";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dPlot setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Plot";
    }

    @Override
    public String identify() {
        return "plot@" + plot.getId().getX() + "," + plot.getId().getZ() + "," + plot.getWorld().getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <plot@plot.id_x>
        // @returns Element(Number)
        // @description
        // Returns the plot's X coordinate portion of its ID.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("id_x")) {
            return new Element(plot.getId().getX()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plot@plot.id_z>
        // @returns Element(Number)
        // @description
        // Returns the plot's Z coordinate portion of its ID.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("id_z")) {
            return new Element(plot.getId().getZ()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plot@plot.world>
        // @returns dWorld
        // @description
        // Returns the plot's world.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("world")) {
            return dWorld.valueOf(plot.getWorld().getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plot@plot.owner>
        // @returns dPlayer
        // @description
        // Returns the plot's owner.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("owner")) {
            return dPlayer.mirrorBukkitPlayer(Bukkit.getOfflinePlayer(plot.getOwnerId())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plot@plot.cuboid>
        // @returns dCuboid
        // @description
        // Returns the plot's cuboid.
        // @Plugin DepenizenBukkit, PlotMe
        // -->
        if (attribute.startsWith("owner")) {
            dWorld world = dWorld.valueOf(plot.getWorld().getName());
            Location l1 = new Location(world.getWorld(), plot.getBottomX(), 0, plot.getBottomZ());
            Location l2 = new Location(world.getWorld(), plot.getTopX(), 255, plot.getTopZ());
            return new dCuboid(l1, l2).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }
}
