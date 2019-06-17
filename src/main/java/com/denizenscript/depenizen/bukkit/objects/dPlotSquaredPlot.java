package com.denizenscript.depenizen.bukkit.objects;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.RegionWrapper;
import com.intellectualcrafters.plot.util.MainUtil;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.*;

import java.util.List;
import java.util.UUID;

public class dPlotSquaredPlot implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static dPlotSquaredPlot valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("plotsquaredplot")
    public static dPlotSquaredPlot valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match plotsquaredplot name

        string = string.replace("plotsquaredplot@", "");
        try {
            List<String> split = CoreUtilities.split(string, ',');
            Plot p = MainUtil.getPlotFromString(null, split.get(2) + ";" + split.get(0) + ";" + split.get(1), false);
            if (p == null) {
                return null;
            }
            return new dPlotSquaredPlot(p);
        }
        catch (Throwable e) {
            dB.echoError(e);
        }
        return null;
    }

    public static boolean matches(String arg) {
        return arg.startsWith("plotsquaredplot@");
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Plot plot = null;

    public dPlotSquaredPlot(Plot pl) {
        plot = pl;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "PlotSquaredPlot";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dPlotSquaredPlot setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return plot.getId().hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof dPlotSquaredPlot) {
            return ((dPlotSquaredPlot) a).plot.getId().equals(plot.getId());
        }
        return false;
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
        return "PlotSquaredPlot";
    }

    @Override
    public String identify() {
        return "plotsquaredplot@" + plot.getId().x + "," + plot.getId().y + "," + plot.getArea().worldname;
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.id_x>
        // @returns Element(Number)
        // @description
        // Returns the plot's X coordinate portion of its ID.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("x")) {
            return new Element(plot.getId().x).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.id_Z>
        // @returns Element(Number)
        // @description
        // Returns the plot's Z coordinate portion of its ID.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("z")) {
            return new Element(plot.getId().y).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.home>
        // @returns dLocation
        // @description
        // Returns the plot's current home location.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("home")) {
            com.intellectualcrafters.plot.object.Location loca = plot.getHome();
            return new dLocation(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.default_home>
        // @returns dLocation
        // @description
        // Returns the plot's default home location.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("default_home")) {
            com.intellectualcrafters.plot.object.Location loca = plot.getDefaultHome();
            return new dLocation(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.world>
        // @returns dWorld
        // @description
        // Returns the plot's world.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("world")) {
            return dWorld.valueOf(plot.getArea().worldname).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.owners>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of all owners of the plot.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("owners")) {
            dList players = new dList();
            for (UUID uuid : plot.getOwners()) {
                players.add(dPlayer.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.trusted>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of all trusted of the plot.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("trusted")) {
            dList players = new dList();
            for (UUID uuid : plot.getTrusted()) {
                players.add(dPlayer.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.members>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of all members of the plot.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("members")) {
            dList players = new dList();
            for (UUID uuid : plot.getMembers()) {
                players.add(dPlayer.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.cuboid>
        // @returns dCuboid
        // @description
        // Returns the plot's cuboid.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("cuboid")) {
            dWorld world = dWorld.valueOf(plot.getArea().worldname);
            Location l1 = new Location(world.getWorld(), plot.getBottomAbs().getX(), 0, plot.getBottomAbs().getZ());
            Location l2 = new Location(world.getWorld(), plot.getTopAbs().getX(), 255, plot.getTopAbs().getZ());
            return new dCuboid(l1, l2).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.all_cuboids>
        // @returns dList(dCuboid)
        // @description
        // Returns all the plot's cuboids in a list. Useful for merged plots.
        // @Plugin DepenizenBukkit, PlotSquared
        // -->
        if (attribute.startsWith("all_cuboids")) {
            dList cuboids = new dList();
            dWorld world = dWorld.valueOf(plot.getArea().worldname);
            for (RegionWrapper region : plot.getRegions()) {
                Location l1 = new Location(world.getWorld(), region.minX, region.minY, region.minZ);
                Location l2 = new Location(world.getWorld(), region.maxX, region.maxY, region.maxZ);
                cuboids.add(new dCuboid(l1, l2).identify());
            }
            return cuboids.getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }

}

