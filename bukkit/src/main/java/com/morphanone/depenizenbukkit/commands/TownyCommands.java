package com.morphanone.depenizenbukkit.commands;

import com.morphanone.depenizenbukkit.objects.dNation;
import com.morphanone.depenizenbukkit.objects.dTown;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

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
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else if (!scriptEntry.hasObject("state")
                    && arg.matchesPrefix("s", "state")
                    && arg.matchesEnum(State.values())) {
                scriptEntry.addObject("state", arg.asElement());
            }

            else if (!scriptEntry.hasObject("town")
                    && arg.matchesArgumentType(dTown.class)) {
                scriptEntry.addObject("town", arg.asType(dTown.class));
            }

            else if (!scriptEntry.hasObject("nation")
                    && arg.matchesArgumentType(dNation.class)) {
                scriptEntry.addObject("nation", arg.asType(dNation.class));
            }

            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(dLocation.class)) {
                scriptEntry.addObject("location", arg.asType(dLocation.class));
            }

            else if (!scriptEntry.hasObject("qty")
                    && arg.matchesPrefix("qty", "q", "quantity")
                    && arg.matchesPrimitive(aH.PrimitiveType.Double)) {
                scriptEntry.addObject("qty", arg.asElement());
            }

            else if (!scriptEntry.hasObject("type")
                    && arg.matchesEnum(Type.values())) {
                scriptEntry.addObject("type", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must specify a valid action!");
        }

        if (!scriptEntry.hasObject("type")) {
            throw new InvalidArgumentsException("Must specify a valid type!");
        }

        scriptEntry.defaultObject("town", new Element("")).defaultObject("nation", "")
                .defaultObject("state", new Element("TOGGLE"));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element action = scriptEntry.getElement("action");
        Element state = scriptEntry.getElement("state");
        Element type = scriptEntry.getElement("type");
        Element town = scriptEntry.getElement("town");
        Element nation = scriptEntry.getElement("nation");
        Element qty = scriptEntry.getElement("qty");
        dLocation location = scriptEntry.getdObject("location");

        dB.report(scriptEntry, getName(), action.debug() + type.debug() + state.debug() + town.debug() + nation.debug()
                + (qty != null ? qty.debug() : "") + (location != null ? location.debug() : ""));

        switch (Type.valueOf(type.asString().toUpperCase())) {
            case RESIDENT:
                break;
            case MONEY:
                break;
            case TOWN:
                break;
            case NATION:
                break;
            case TOWNBLOCK:
                break;
            case BONUS:
                break;
            case PLOT:
                break;
            case OUTPOST:
                break;
            case NAME:
                break;
            case CAPITAL:
                break;
            case OPEN:
                break;
            case PUBLIC:
                break;
            case MAYOR:
                break;
            case SURNAME:
                break;
            case TITLE:
                break;
            case RANK:
                break;
            case BOARD:
                break;
            case WAR:
                break;
            case MAXSIZE:
                break;
            case TAXES:
                break;
            case TAG:
                break;
            case SPAWN:
                break;
            case PERM:
                break;
            case RELATION:
                break;
        }

    }
}
