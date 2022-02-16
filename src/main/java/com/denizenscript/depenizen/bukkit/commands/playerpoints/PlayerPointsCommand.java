package com.denizenscript.depenizen.bukkit.commands.playerpoints;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bridges.PlayerPointsBridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import org.black_ixx.playerpoints.PlayerPoints;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class PlayerPointsCommand extends AbstractCommand {

    public PlayerPointsCommand() {
        setName("playerpoints");
        setSyntax("playerpoints [set/give/take] [<#>]");
        setRequiredArguments(2, 2);
    }

    // <--[command]
    // @Name playerpoints
    // @Syntax playerpoints [set/give/take] [<#>]
    // @Group Depenizen
    // @Plugin Depenizen, PlayerPoints
    // @Required 2
    // @Maximum 2
    // @Short Adjusts the amount of points the player has.
    //
    // @Description
    // Take, give or set the amount of points a player has.
    // This is useful for plugins supporting this kind of economy
    // which uses the points instead of money as an alternative system.
    // This works for offline players.
    //
    // @Tags
    // <PlayerTag.playerpoints_points>
    //
    // @Usage
    // Use to give 5 points to the player.
    // - playerpoints give 5
    //
    // @Usage
    // Use to set 10 points to the player.
    // - playerpoints set 10
    //
    // -->

    private enum Action {SET, GIVE, TAKE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(PlayerTag.class));
                Debug.echoError("Don't use 'target:' for 'playerpoints' command. Just use 'player:'.");
            }
            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }
            else if (!scriptEntry.hasObject("amount")) {
                scriptEntry.addObject("amount", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified! (remove/mob/player/misc)");
        }
        if (!scriptEntry.hasObject("amount")) {
            throw new InvalidArgumentsException("Amount not specified!");
        }
        if (!scriptEntry.hasObject("target")) {
            if (Utilities.entryHasPlayer(scriptEntry)) {
                scriptEntry.addObject("target", Utilities.getEntryPlayer(scriptEntry));
            }
            else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        PlayerTag target = scriptEntry.getObjectTag("target");
        ElementTag action = scriptEntry.getObjectTag("action");
        ElementTag amount = scriptEntry.getObjectTag("amount");
        Debug.report(scriptEntry, getName(), action, target, amount);
        if (target == null) {
            Debug.echoError(scriptEntry, "Target not found!");
            return;
        }
        if (amount == null) {
            Debug.echoError(scriptEntry, "Entity not specified!");
            return;
        }
        PlayerPoints plugin = (PlayerPoints) PlayerPointsBridge.instance.plugin;
        if (action.asString().equalsIgnoreCase("give")) {
            plugin.getAPI().give(target.getUUID(), amount.asInt());
        }
        else if (action.asString().equalsIgnoreCase("take")) {
            plugin.getAPI().take(target.getUUID(), amount.asInt());
        }
        else if (action.asString().equalsIgnoreCase("set")) {
            plugin.getAPI().set(target.getUUID(), amount.asInt());
        }
    }
}
