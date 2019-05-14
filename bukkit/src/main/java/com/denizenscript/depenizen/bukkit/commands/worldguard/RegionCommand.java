package com.denizenscript.depenizen.bukkit.commands.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionCommand extends AbstractCommand {

    // <--[command]
    // @Name region
    // @Syntax region [{add} <cuboid>/remove <world>] [id:<name>]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, WorldGuard
    // @Required 2
    // @Short Adds or removes a protected region.

    // @Description
    // Adds a protected region to a region manager based on the specified cuboid,
    // or removes a protected region from a region manager based on the specified
    // world. Currently, this command only supports cuboid-shaped regions.

    // @Tags None

    // @Usage
    // Use to add a region based on a cuboid.
    // - region add cu@l@123,0,321,world|l@321,256,123,world id:MyRegion

    // @Usage
    // Use to remove a region from a world.
    // - region remove w@world id:MyRegion

    // -->

    private enum Action {ADD, REMOVE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("region_id")
                    && arg.matchesPrefix("id")) {
                scriptEntry.addObject("region_id", arg.asElement());
            }

            else if (!scriptEntry.hasObject("cuboid")
                    && arg.matchesArgumentType(dCuboid.class)) {
                scriptEntry.addObject("cuboid", arg.asType(dCuboid.class));
            }

            else if (!scriptEntry.hasObject("world")
                    && arg.matchesArgumentType(dWorld.class)) {
                scriptEntry.addObject("world", arg.asType(dWorld.class));
            }

            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
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
            scriptEntry.addObject("action", new Element("ADD"));
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        Element region_id = scriptEntry.getElement("region_id");
        dCuboid cuboid = scriptEntry.getdObject("cuboid");
        dWorld w = scriptEntry.getdObject("world");
        World world = w != null ? w.getWorld() : cuboid != null ? cuboid.getWorld() : null;
        Element action = scriptEntry.getElement("action");

        if (world == null) {
            dB.echoError("No valid world found!");
            return;
        }

        dB.report(scriptEntry, getName(), region_id.debug() + (cuboid != null ? cuboid.debug() : "")
                + aH.debugObj("world", world.getName()) + action.debug());

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
