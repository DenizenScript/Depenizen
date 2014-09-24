package net.gnomeffinway.depenizen.commands;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;

public class TownyCommands extends AbstractCommand {

    // TODO: make this work

    private enum Action {
        ADD, REMOVE, SET
    }

    private enum State {TRUE, FALSE, TOGGLE}

    private enum Type {
        RESIDENT, MONEY, TOWN, NATION, TOWNBLOCK, BONUS, PLOT, OUTPOST, NAME,
        CAPITAL, OPEN, PUBLIC, MAYOR, SURNAME, TITLE, RANK, BOARD, WAR, MAXSIZE, TAXES, TAG, SPAWN, PERM, RELATION
    }

    public TownyCommands() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values()))
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));

            else if (!scriptEntry.hasObject("state")
                    && arg.matchesPrefix("s", "state")
                    && arg.matchesEnum(State.values()))
                scriptEntry.addObject("state", State.valueOf(arg.getValue().toUpperCase()));

            else if (!scriptEntry.hasObject("town")
                    && arg.matchesArgumentType(dTown.class))
                scriptEntry.addObject("town", arg.asType(dTown.class));

            else if (!scriptEntry.hasObject("nation")
                    && arg.matchesArgumentType(dNation.class))
                scriptEntry.addObject("nation", arg.asType(dNation.class));

            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(dLocation.class))
                scriptEntry.addObject("location", arg.asType(dLocation.class));

            else if (!scriptEntry.hasObject("qty")
                    && arg.matchesPrefix("qty", "q", "quantity")
                    && arg.matchesPrimitive(aH.PrimitiveType.Double))
                scriptEntry.addObject("qty", arg.asElement());

            else if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values()))
                scriptEntry.addObject("type", Type.valueOf(arg.getValue().toUpperCase()));

        }

        scriptEntry.defaultObject("town", new Element("")).defaultObject("nation", "")
                .defaultObject("state", State.TOGGLE).defaultObject("qty", new Element(-1));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {


    }
}
