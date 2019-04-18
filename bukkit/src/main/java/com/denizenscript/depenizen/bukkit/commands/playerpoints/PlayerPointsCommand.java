package com.denizenscript.depenizen.bukkit.commands.playerpoints;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.PlayerPointsSupport;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import org.black_ixx.playerpoints.PlayerPoints;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.plugin.Plugin;

public class PlayerPointsCommand extends AbstractCommand {
    // <--[command]
    // @Name playerpoints
    // @Syntax playerpoints [set/give/take] (amount:<amount>) (target:<player>)
    // @Group Depenizen
    // @Plugin DepenizenBukkit, PlayerPoints
    // @Required 2
    // @Stable stable
    // @Short Adjusts the amount of points the player has.
    // @Author Mwthorn

    // @Description
    // Take, give or set the amount of points a player has.
    // This is useful for plugins supporting this kind of economy
    // which uses the points instead of money as an alternative system.
    // This works for offline players.

    // @Tags
    // <p@player.playerpoints_points>

    // @Usage
    // Use to give 5 points to the player
    // - playerpoints give 5

    // @Usage
    // Use take 2 points from a target player
    // - playerpoints take 2 target:p@Mwthorn

    // @Usage
    // Use to set 10 points to the player
    // - playerpoints set 10

    // -->

    private enum Action {SET, GIVE, TAKE}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("target")
                    && arg.matchesPrefix("target")) {
                scriptEntry.addObject("target", arg.asType(dPlayer.class));
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
            if (((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
                scriptEntry.addObject("target", ((BukkitScriptEntryData) scriptEntry.entryData).getPlayer());
            }
            else {
                throw new InvalidArgumentsException("This command does not have a player attached!");
            }
        }

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        dPlayer target = scriptEntry.getdObject("target");
        Element action = scriptEntry.getdObject("action");
        Element amount = scriptEntry.getdObject("amount");

        // Report to dB
        dB.report(scriptEntry, getName(), action.debug()
                + (target != null ? target.debug() : "")
                + (amount != null ? amount.debug() : ""));

        if (target == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Target not found!");
            return;
        }

        if (amount == null) {
            dB.echoError(scriptEntry.getResidingQueue(), "Entity not specified!");
            return;
        }

        if (action.asString().equalsIgnoreCase("give")) {
            Plugin plugin = Support.getPlugin(PlayerPointsSupport.class);
            ((PlayerPoints) plugin).getAPI().give(target.getOfflinePlayer().getUniqueId(), amount.asInt());
        }

        else if (action.asString().equalsIgnoreCase("take")) {
            Plugin plugin = Support.getPlugin(PlayerPointsSupport.class);
            ((PlayerPoints) plugin).getAPI().take(target.getOfflinePlayer().getUniqueId(), amount.asInt());
        }

        else if (action.asString().equalsIgnoreCase("set")) {
            Plugin plugin = Support.getPlugin(PlayerPointsSupport.class);
            ((PlayerPoints) plugin).getAPI().set(target.getOfflinePlayer().getUniqueId(), amount.asInt());
        }

    }
}
