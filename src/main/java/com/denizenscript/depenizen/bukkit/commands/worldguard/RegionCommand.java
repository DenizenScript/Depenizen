package com.denizenscript.depenizen.bukkit.commands.worldguard;

import com.denizenscript.denizencore.objects.Argument;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionCommand extends AbstractCommand {

    public RegionCommand() {
        setName("region");
        setSyntax("region [{add} <cuboid>/remove <world>] [id:<name>]");
        setRequiredArguments(2, 3);
    }

    // <--[command]
    // @Name region
    // @Syntax region [{add} <cuboid>/remove <world>] [id:<name>]
    // @Group Depenizen
    // @Plugin Depenizen, WorldGuard
    // @Required 2
    // @Maximum 3
    // @Short Adds or removes a protected region.
    //
    // @Description
    // Adds a protected region to a region manager based on the specified cuboid,
    // or removes a protected region from a region manager based on the specified
    // world. Currently, this command only supports cuboid-shaped regions.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to add a region based on a cuboid.
    // - region add <[some_cuboid]> id:MyRegion
    //
    // @Usage
    // Use to remove a region from a world.
    // - region remove world id:MyRegion
    //
    // -->

    private enum Action {ADD, REMOVE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("region_id")
                    && arg.matchesPrefix("id")) {
                scriptEntry.addObject("region_id", arg.asElement());
            }
            else if (!scriptEntry.hasObject("cuboid")
                    && arg.matchesArgumentType(CuboidTag.class)) {
                scriptEntry.addObject("cuboid", arg.asType(CuboidTag.class));
            }
            else if (!scriptEntry.hasObject("world")
                    && arg.matchesArgumentType(WorldTag.class)) {
                scriptEntry.addObject("world", arg.asType(WorldTag.class));
            }
            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.class)) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("region_id")) {
            throw new InvalidArgumentsException("Must specify a region id!");
        }
        if (!scriptEntry.hasObject("cuboid") && (!scriptEntry.hasObject("action")
                || scriptEntry.getElement("action").asString().equalsIgnoreCase("ADD"))) {
            throw new InvalidArgumentsException("Must specify a valid cuboid!");
        }
        if (!scriptEntry.hasObject("world") && scriptEntry.hasObject("action")
                && scriptEntry.getElement("action").asString().equalsIgnoreCase("REMOVE")) {
            throw new InvalidArgumentsException("Must specify a valid world!");
        }
        if (!scriptEntry.hasObject("action")) {
            scriptEntry.addObject("action", new ElementTag("ADD"));
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag region_id = scriptEntry.getElement("region_id");
        CuboidTag cuboid = scriptEntry.getObjectTag("cuboid");
        WorldTag w = scriptEntry.getObjectTag("world");
        World world = w != null ? w.getWorld() : cuboid != null ? cuboid.getWorld().getWorld() : null;
        ElementTag action = scriptEntry.getElement("action");
        if (world == null) {
            Debug.echoError("No valid world found!");
            return;
        }
        Debug.report(scriptEntry, getName(), region_id, cuboid, db("world", world.getName()), action);
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (action.asString().equalsIgnoreCase("REMOVE")) {
            regionManager.removeRegion(region_id.asString());
            return;
        }
        Location low = cuboid.getLow(0);
        Location high = cuboid.getHigh(0);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(region_id.asString(),
                BlockVector3.at(low.getX(), low.getY(), low.getZ()),
                BlockVector3.at(high.getX(), high.getY(), high.getZ()));
        regionManager.addRegion(region);
    }
}
