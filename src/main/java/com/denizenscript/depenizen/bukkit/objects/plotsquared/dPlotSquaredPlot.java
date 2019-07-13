package com.denizenscript.depenizen.bukkit.objects.plotsquared;

import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.RegionWrapper;
import com.github.intellectualsites.plotsquared.plot.util.MainUtil;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.*;

import java.util.List;
import java.util.UUID;

public class dPlotSquaredPlot implements ObjectTag {

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
            Debug.echoError(e);
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
    //   ObjectTag Methods
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
        // @returns ElementTag(Number)
        // @description
        // Returns the plot's X coordinate portion of its ID.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("x")) {
            return new ElementTag(plot.getId().x).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.id_Z>
        // @returns ElementTag(Number)
        // @description
        // Returns the plot's Z coordinate portion of its ID.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("z")) {
            return new ElementTag(plot.getId().y).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.home>
        // @returns LocationTag
        // @description
        // Returns the plot's current home location.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("home")) {
            com.github.intellectualsites.plotsquared.plot.object.Location loca = plot.getHome();
            return new LocationTag(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.default_home>
        // @returns LocationTag
        // @description
        // Returns the plot's default home location.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("default_home")) {
            com.github.intellectualsites.plotsquared.plot.object.Location loca = plot.getDefaultHome();
            return new LocationTag(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.world>
        // @returns WorldTag
        // @description
        // Returns the plot's world.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("world")) {
            return WorldTag.valueOf(plot.getArea().worldname).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.owners>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of all owners of the plot.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("owners")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getOwners()) {
                players.add(PlayerTag.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.trusted>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of all trusted of the plot.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("trusted")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getTrusted()) {
                players.add(PlayerTag.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.members>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of all members of the plot.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("members")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getMembers()) {
                players.add(PlayerTag.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.cuboid>
        // @returns CuboidTag
        // @description
        // Returns the plot's cuboid.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("cuboid")) {
            WorldTag world = WorldTag.valueOf(plot.getArea().worldname);
            Location l1 = new Location(world.getWorld(), plot.getBottomAbs().getX(), 0, plot.getBottomAbs().getZ());
            Location l2 = new Location(world.getWorld(), plot.getTopAbs().getX(), 255, plot.getTopAbs().getZ());
            return new CuboidTag(l1, l2).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <plotsquaredplot@plotsquaredplot.all_cuboids>
        // @returns ListTag(CuboidTag)
        // @description
        // Returns all the plot's cuboids in a list. Useful for merged plots.
        // @Plugin Depenizen, PlotSquared
        // -->
        if (attribute.startsWith("all_cuboids")) {
            ListTag cuboids = new ListTag();
            WorldTag world = WorldTag.valueOf(plot.getArea().worldname);
            for (RegionWrapper region : plot.getRegions()) {
                Location l1 = new Location(world.getWorld(), region.minX, region.minY, region.minZ);
                Location l2 = new Location(world.getWorld(), region.maxX, region.maxY, region.maxZ);
                cuboids.add(new CuboidTag(l1, l2).identify());
            }
            return cuboids.getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}

