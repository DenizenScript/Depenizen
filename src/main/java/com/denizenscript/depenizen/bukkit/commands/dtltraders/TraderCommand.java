package com.denizenscript.depenizen.bukkit.commands.dtltraders;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.dandielo.api.traders.TraderAPI;
import net.dandielo.citizens.traders_v3.TEntityStatus;
import net.dandielo.citizens.traders_v3.traits.TraderTrait;

// Based on the class in dtlTraders itself
public class TraderCommand extends AbstractCommand {

    // <--[command]
    // @Name Trader
    // @Syntax trader [open/close] ({buy}/sell) ({stock}/relation)
    // @Group Depenizen
    // @Plugin Depenizen, dtlTraders
    // @Required 1
    // @Short Opens or closes an NPC's trading menu.

    // @Description
    // This command allows you to open or close the trading menu of an NPC
    // that has the Trader trait from dtlTraders.
    // You can specify whether to open the buy or sell menu, with default as buying.
    // Optionally, you can specify whether to open the stock menu or relations menu.
    // This command requires an attached player and NPC.

    // @Tags
    // None

    // @Usage
    // Use to open the buy menu of an attached NPC.
    // - trader open buy

    // @Usage
    // Use to close an open trading menu.
    // - trader close

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Interpret arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            }

            else if (arg.matches("buy", "sell")) {
                scriptEntry.addObject("status", TEntityStatus.valueOf(arg.getValue().toUpperCase()));
            }

            if (arg.matchesEnum(Context.values())) {
                scriptEntry.addObject("context", Context.valueOf(arg.getValue().toUpperCase()));
            }
        }


        BukkitScriptEntryData data = (BukkitScriptEntryData) scriptEntry.entryData;

        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Must have action specified!");
        }
        else if (!data.hasPlayer()) {
            throw new InvalidArgumentsException("Requires a player for executing!");
        }
        else if (!data.hasNPC()) {
            throw new InvalidArgumentsException("Requires a Trader NPC for executing!");
        }
        if (!data.getNPC().getCitizen().hasTrait(TraderTrait.class)) {
            throw new InvalidArgumentsException("Requires a Trader NPC for executing!");
        }

        scriptEntry.defaultObject("status", TEntityStatus.BUY).defaultObject("context", Context.STOCK);
    }


    @Override
    public void execute(ScriptEntry scriptEntry) {

        // Fetch required objects
        Action action = (Action) scriptEntry.getObject("action");
        TEntityStatus status = (TEntityStatus) scriptEntry.getObject("status");
        Context context = (Context) scriptEntry.getObject("context");
        TraderTrait trait = ((BukkitScriptEntryData) scriptEntry.entryData).getNPC().getCitizen().getTrait(TraderTrait.class);

        // Debug the execution
        dB.report(scriptEntry, getName(), aH.debugObj("Action", action.toString())
                + aH.debugObj("Status", status)
                + aH.debugObj("Context", context));

        // Do the execution
        switch (action) {
            case OPEN:
                TraderAPI.openTrader(((BukkitScriptEntryData) scriptEntry.entryData).getPlayer().getPlayerEntity(), trait, status, context == Context.STOCK);
                break;
            case CLOSE:
                TraderAPI.closeTrader(((BukkitScriptEntryData) scriptEntry.entryData).getPlayer().getPlayerEntity());
                break;
            default:
                break;
        }
    }

    private enum Context {
        STOCK, RELATION
    }

    private enum Action {
        OPEN, CLOSE
    }
}
