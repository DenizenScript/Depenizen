package net.gnomeffinway.depenizen.battlenight;

import org.bukkit.entity.Player;

import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

public class BattleNightCommands extends AbstractCommand {
    //TODO: CLASS ?
    private enum Action {ADD, KICK, START, END}
    
    public BattleNightCommands() {
        
    }
    
    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        // Initialize fields used
        Action action = null;
        Player player = scriptEntry.getPlayer();
        boolean spectator = false;
        
        // Iterate through arguments
        for (String arg : scriptEntry.getArguments()) {
            if (aH.matchesArg("ADD, KICK, START, END", arg)) {
                action = Action.valueOf(aH.getStringFrom(arg).toUpperCase());
            } else if (aH.matchesValueArg("SPECTATOR", arg, aH.ArgumentType.String)) {
                spectator = aH.getBooleanFrom(arg);
            } else if (aH.matchesValueArg("PLAYER", arg, aH.ArgumentType.String)) {
                player = aH.getPlayerFrom(arg);
            }else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
            
        }
        
        // Stash objects in scriptEntry for use in execute()
        scriptEntry.addObject("action", action).addObject("player", player).addObject("spectator", spectator);
    }
    
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        // Get objects
        Action action = (Action) scriptEntry.getObject("action");
        Player player = (Player) scriptEntry.getObject("player");
        boolean spectator = Boolean.valueOf(String.valueOf(scriptEntry.getObject("spectator")));

        // Report to dB
        dB.report(getName(),
                aH.debugObj("NPC", scriptEntry.getNPC().toString())
                        + aH.debugObj("Action", action.toString())
                        + aH.debugObj("Player", player.getName())
                        + aH.debugObj("Spectator", String.valueOf(spectator)));

        switch (action) {
            case ADD: {
                if(spectator) {
                    BattleNight.instance.getAPI().getBattle().addPlayer(player);
                    BattleNight.instance.getAPI().getBattle().toSpectator(player, true);
                } else {
                    BattleNight.instance.getAPI().getBattle().addPlayer(player);
                }
                return;
            }
            case KICK: {
                BattleNight.instance.getAPI().getBattle().removePlayer(player);
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
