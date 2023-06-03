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
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.*;

import java.util.List;
import java.util.UUID;

public class PlotSquaredPlotTag implements ObjectTag {

    // <--[ObjectType]
    // @name PlotSquaredPlotTag
    // @prefix plotsquaredplot
    // @base ElementTag
    // @format
    // The identity format for plots is <x>,<z>,<world>
    // For example, 'plotsquaredplot@5,10,Hub'.
    //
    // @plugin Depenizen, PlotSquared
    // @description
    // A PlotSquaredPlotTag represents a PlotSquared plot in the world.
    //
    // -->

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("plotsquaredplot")
    public static PlotSquaredPlotTag valueOf(String string, TagContext context) {
        if (string.startsWith("plotsquaredplot@")) {
            string = string.substring("plotsquaredplot@".length());
        }
        try {
            List<String> split = CoreUtilities.split(string, ',');
            if (split.size() < 3) {
                return null;
            }
            Plot p = MainUtil.getPlotFromString(null, split.get(2) + ";" + split.get(0) + ";" + split.get(1), false);
            if (p == null) {
                return null;
            }
            return new PlotSquaredPlotTag(p);
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

    public Plot plot;

    public PlotSquaredPlotTag(Plot pl) {
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
    public PlotSquaredPlotTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public int hashCode() {
        return plot.getId().hashCode();
    }

    @Override
    public boolean equals(Object a) {
        if (a instanceof PlotSquaredPlotTag) {
            return ((PlotSquaredPlotTag) a).plot.getId().equals(plot.getId());
        }
        return false;
    }

    @Override
    public boolean isUnique() {
        return true;
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
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.id_x>
        // @returns ElementTag(Number)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's X coordinate portion of its ID.
        // -->
        if (attribute.startsWith("x")) {
            return new ElementTag(plot.getId().x).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.id_Z>
        // @returns ElementTag(Number)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's Z coordinate portion of its ID.
        // -->
        if (attribute.startsWith("z")) {
            return new ElementTag(plot.getId().y).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.home>
        // @returns LocationTag
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's current home location.
        // -->
        if (attribute.startsWith("home")) {
            com.github.intellectualsites.plotsquared.plot.object.Location loca = plot.getHome();
            return new LocationTag(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.default_home>
        // @returns LocationTag
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's default home location.
        // -->
        if (attribute.startsWith("default_home")) {
            com.github.intellectualsites.plotsquared.plot.object.Location loca = plot.getDefaultHome();
            return new LocationTag(new Location(Bukkit.getWorld(plot.getArea().worldname), loca.getX(), loca.getY(), loca.getZ())).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.world>
        // @returns WorldTag
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's world.
        // -->
        if (attribute.startsWith("world")) {
            return WorldTag.valueOf(plot.getArea().worldname, attribute.context).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.owners>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns a list of all owners of the plot.
        // -->
        if (attribute.startsWith("owners")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getOwners()) {
                players.addObject(new PlayerTag(uuid));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.trusted>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns a list of all trusted of the plot.
        // -->
        if (attribute.startsWith("trusted")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getTrusted()) {
                players.addObject(new PlayerTag(uuid));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.denied>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns a list of all players denied from the plot.
        // -->
        if (attribute.startsWith("denied")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getDenied()) {
                players.addObject(new PlayerTag(uuid));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns a list of all members of the plot.
        // -->
        if (attribute.startsWith("members")) {
            ListTag players = new ListTag();
            for (UUID uuid : plot.getMembers()) {
                players.addObject(new PlayerTag(uuid));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.cuboid>
        // @returns CuboidTag
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns the plot's cuboid.
        // -->
        if (attribute.startsWith("cuboid")) {
            WorldTag world = WorldTag.valueOf(plot.getArea().worldname, attribute.context);
            Location l1 = new Location(world.getWorld(), plot.getBottomAbs().getX(), 0, plot.getBottomAbs().getZ());
            Location l2 = new Location(world.getWorld(), plot.getTopAbs().getX(), 255, plot.getTopAbs().getZ());
            return new CuboidTag(l1, l2).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlotSquaredPlotTag.all_cuboids>
        // @returns ListTag(CuboidTag)
        // @plugin Depenizen, PlotSquared
        // @description
        // Returns all the plot's cuboids in a list. Useful for merged plots.
        // -->
        if (attribute.startsWith("all_cuboids")) {
            ListTag cuboids = new ListTag();
            WorldTag world = WorldTag.valueOf(plot.getArea().worldname, attribute.context);
            for (RegionWrapper region : plot.getRegions()) {
                Location l1 = new Location(world.getWorld(), region.minX, region.minY, region.minZ);
                Location l2 = new Location(world.getWorld(), region.maxX, region.maxY, region.maxZ);
                cuboids.addObject(new CuboidTag(l1, l2));
            }
            return cuboids.getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);

    }
}
