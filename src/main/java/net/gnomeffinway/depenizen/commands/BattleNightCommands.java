package net.gnomeffinway.depenizen.commands;

import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class BattleNightCommands extends AbstractCommand {

    // <--[command]
    // @Name BN
    // @Syntax bn [add/kick/start/end]
    // @Plugin BattleNight
    // @Required 1
    // @Stable stable
    // @Short Adds/kicks player, starts/ends battle.
    // @Author GnomeffinWay

    // @Description
    // With this command, you can add or kick players to/from the current battle. By default,
    // this uses the player currently attached to the running Denizen queue. However, you can
    // change the player by using Denizen's command-wide "player:" prefix. You can also use
    // this command to start or end a battle.

    // @Tags None

    // @Usage
    // Use to add the current player to a battle.
    // - bn add

    // @Usage
    // Use to kick unattached player from a battle.
    // - bn kick player:BlackCoyote

    // @Usage
    // Use to start a battle.
    // - bn start

    // @Usage
    // Use to end a battle.
    // - bn end

    // -->

    private enum Action {
        ADD, KICK, START, END
    }

    public BattleNightCommands() {

    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            }

            // The player:<name> argument is handled in Denizen automatically. No need to re-create it here

            // It seems that spectator mode was removed in the latest version of BattleNight...
            // Looks like they're going to find a better way to do it. Until then, let's just
            // keep this code commented out.
            
            /*
            Allow - bn (spectator), which is more 0.9-like.
            else if (!scriptEntry.hasObject("spectator")
            		&& arg.matches("spectator"))
                scriptEntry.addObject("spectator", Element.TRUE);
            
            // Keep old format for backwards compatibility
            else if (!scriptEntry.hasObject("spectator")
            		&& arg.matchesPrefix("spectator")
            		&& arg.matchesPrimitive(aH.PrimitiveType.Boolean))
            	scriptEntry.addObject("spectator", arg.asElement());
            */

        }

        // Stash objects in scriptEntry for use in execute()
        if (!scriptEntry.hasObject("action"))
            throw new InvalidArgumentsException("Must specify an action!");

        // scriptEntry.defaultObject("spectator", Element.FALSE);
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        // Get objects
        Action action = (Action) scriptEntry.getObject("action");
        dPlayer player = scriptEntry.getPlayer();
        // boolean spectator = scriptEntry.getElement("spectator").asBoolean();

        // Report to dB
        dB.report(scriptEntry, getName(),
                aH.debugObj("NPC", scriptEntry.getNPC().toString())
                        + aH.debugObj("Action", action.toString())
                        // + aH.debugObj("Spectator", String.valueOf(spectator))
                        + aH.debugObj("Player", player.getName()));

        switch (action) {

            case ADD: {
                /*
                if(spectator) {
                    BattleNight.instance.getAPI().getBattle().addPlayer(player.getPlayerEntity());
                    BattleNight.instance.getAPI().getBattle().toSpectator(player.getPlayerEntity(), true);
                } else {
                   BattleNight.instance.getAPI().getBattle().addPlayer(player.getPlayerEntity());
                }
            	*/

                // Add the player
                BattleNight.instance.getAPI().getBattle().addPlayer(player.getPlayerEntity());

                return;
            }

            case KICK: {
                BattleNight.instance.getAPI().getBattle().removePlayer(player.getPlayerEntity());
                return;
            }

            case START: {
                BattleNight.instance.getAPI().getBattle().start();
                return;
            }

            case END: {
                BattleNight.instance.getAPI().getBattle().stop();
                return;
            }

        }

    }
}
