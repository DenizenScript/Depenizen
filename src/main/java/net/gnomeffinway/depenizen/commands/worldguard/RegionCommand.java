package net.gnomeffinway.depenizen.commands.worldguard;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionCommand extends AbstractCommand {

    // TODO: finish and meta-fy this

    private enum Action { ADD, REMOVE }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("region_id")
                    && arg.matchesPrefix("id"))
                scriptEntry.addObject("region_id", arg.asElement());

            if (!scriptEntry.hasObject("cuboid")
                    && arg.matchesArgumentType(dCuboid.class))
                scriptEntry.addObject("cuboid", arg.asType(dCuboid.class));

            if (!scriptEntry.hasObject("world")
                    && arg.matchesArgumentType(dWorld.class))
                scriptEntry.addObject("world", arg.asType(dWorld.class));

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values()))
                scriptEntry.addObject("action", arg.asElement());

        }

        if (!scriptEntry.hasObject("region_id"))
            throw new InvalidArgumentsException("Must specify a region id!");

        if (!scriptEntry.hasObject("cuboid") && (!scriptEntry.hasObject("action")
                || scriptEntry.getElement("action").asString().equalsIgnoreCase("ADD")))
            throw new InvalidArgumentsException("Must specify a valid cuboid!");

        if (!scriptEntry.hasObject("action"))
            scriptEntry.addObject("action", new Element("ADD"));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element region_id = scriptEntry.getElement("region_id");
        dCuboid cuboid = scriptEntry.hasObject("cuboid") ? scriptEntry.getdObjectAs("cuboid", dCuboid.class) : null;
        World world = scriptEntry.hasObject("world") ? scriptEntry.getdObjectAs("world", dWorld.class).getWorld()
                : cuboid != null ? cuboid.getWorld()
                : scriptEntry.hasPlayer() ? scriptEntry.getPlayer().getWorld()
                : scriptEntry.hasNPC() ? scriptEntry.getNPC().getWorld() : null;
        Element action = scriptEntry.getElement("action");

        if (world == null)
            throw new CommandExecutionException("No valid world found!");

        dB.report(scriptEntry, getName(), region_id.debug() + (cuboid != null ? cuboid.debug() : "")
                + aH.debugObj("world", world.getName()) + action.debug());

        if (action.asString().equalsIgnoreCase("REMOVE")) {
            Depenizen.worldguard.getRegionManager(world).removeRegion(region_id.asString());
            return;
        }

        Location low = cuboid.getLow(0);
        Location high = cuboid.getHigh(0);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(region_id.asString(),
                new BlockVector(low.getX(), low.getY(), low.getZ()),
                new BlockVector(high.getX(), high.getY(), high.getZ()));

        Depenizen.worldguard.getRegionManager(cuboid.getWorld()).addRegion(region);

    }

}
