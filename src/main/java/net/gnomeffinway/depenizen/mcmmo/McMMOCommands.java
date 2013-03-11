package net.gnomeffinway.depenizen.mcmmo;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.database.DatabaseManager;
import com.gmail.nossr50.database.LeaderboardManager;
import com.gmail.nossr50.party.PartyManager;

public class McMMOCommands extends AbstractCommand {
    //TODO: CLASS ?
    private enum Action {ADD, REMOVE, SET}
    private enum State {TRUE, FALSE, TOGGLE} 
    private enum Type {XP, LEVELS, TOGGLE, XPRATE, LEADER, VAMPIRISM, HARDCORE} 

    public McMMOCommands() {
        
    }
    
    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        // Initialize fields used
        Action action = null;
        Player player = null;
        String party = "";
        Type type = null;
        String skill = "";
        State state = State.TOGGLE;
        double qty = -1;
        
        // Iterate through arguments
        for (String arg : scriptEntry.getArguments()) {
            if (aH.matchesArg("ADD, REMOVE, SET", arg)) {
                action = Action.valueOf(aH.getStringFrom(arg).toUpperCase());
            } else if (aH.matchesValueArg("STATE", arg, aH.ArgumentType.String)) {
                if (aH.getStringFrom(arg).equalsIgnoreCase("TRUE")) {
                    state = State.TRUE;
                } else if (aH.getStringFrom(arg).equalsIgnoreCase("FALSE")) {
                    state = State.FALSE;
                } else if (aH.getStringFrom(arg).equalsIgnoreCase("TOGGLE")) {
                    state = State.TOGGLE;
                } else dB.echoError("Unknown STATE! Valid: TRUE, FALSE, TOGGLE");
            } else if (aH.matchesValueArg("PLAYER", arg, aH.ArgumentType.String)) {
                player = aH.getPlayerFrom(arg);
            } else if (aH.matchesValueArg("PARTY", arg, aH.ArgumentType.String)) {
                party = aH.getStringFrom(arg);
            } else if (aH.matchesValueArg("SKILL", arg, aH.ArgumentType.String)) {
                skill = aH.getStringFrom(arg);
            } else if (aH.matchesValueArg("QTY", arg, aH.ArgumentType.String)) {
                qty = aH.getDoubleFrom(arg);
            } else if (aH.matchesArg("XP, LEVELS, TOGGLE, XPRATE, VAMPIRISM, HARDCORE", arg)) {
                type = Type.valueOf(aH.getStringFrom(arg).toUpperCase());
            } else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
            
        }
        
        // Stash objects in scriptEntry for use in execute()
        scriptEntry.addObject("action", action).addObject("player", player).addObject("state", state)
                .addObject("qty", qty).addObject("type", type).addObject("party", party).addObject("skill", skill);
    }
    
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        // Get objects
        Action action = (Action) scriptEntry.getObject("action");
        Player tempPlayer = (Player) scriptEntry.getObject("player");
        Player player = tempPlayer != null ? tempPlayer: scriptEntry.getPlayer();
        State state = State.valueOf(String.valueOf(scriptEntry.getObject("state")));
        double qty = Double.valueOf(String.valueOf(scriptEntry.getObject("qty")));
        String party = String.valueOf(scriptEntry.getObject("party"));
        String skill = String.valueOf(scriptEntry.getObject("skill"));
        Type type = (Type) scriptEntry.getObject("type");

        // Report to dB
        dB.report(getName(),
                aH.debugObj("NPC", scriptEntry.getNPC().toString())
                        + aH.debugObj("Action", action.toString())
                        + aH.debugObj("Player", player.getName())
                        + aH.debugObj("State", String.valueOf(state))
                        + aH.debugObj("Type", String.valueOf(type))
                        + aH.debugObj("Party", String.valueOf(party))
                        + aH.debugObj("Skill", String.valueOf(skill))
                        + aH.debugObj("Qty", String.valueOf(qty)));

        switch (action) {
            case ADD: {
                if(qty >= 0 && !skill.equals("")) {
                    if(type == Type.LEVELS) {
                        ExperienceAPI.addLevel(player, skill, (int) qty);
                    } else if(type == Type.XP) {
                        ExperienceAPI.addRawXP(player, skill, (int) qty);
                    }
                } else if(PartyManager.isParty(party)) {
                    PartyAPI.addToParty(player,party);
                }
                return;
            }
            case REMOVE: {
                if(qty >= 0 && !skill.equals("")) {
                    if(type == Type.LEVELS) {
                        ExperienceAPI.setLevel(player, skill, ExperienceAPI.getLevel(player, skill) - (int) qty);
                    } else if(type == Type.XP) {
                        ExperienceAPI.removeXP(player, skill, (int) qty);
                    }
                } else if(PartyManager.isParty(party)) {
                    if(tempPlayer == null) {
                        PartyManager.disbandParty(PartyManager.getParty(party));
                    } else if(PartyAPI.getPartyLeader(party).equals(player.getName())) {
                        PartyManager.disbandParty(PartyManager.getParty(party));
                    } else {
                        PartyAPI.removeFromParty(player);
                    }
                } else if(tempPlayer != null){
                    if (Config.getInstance().getUseMySQL()) {
                        String tablePrefix = Config.getInstance().getMySQLTablePrefix();

                        if (DatabaseManager.update("DELETE FROM " + tablePrefix + "users WHERE " + tablePrefix + "users.user = '" + player.getName() + "'") != 0) {
                            DatabaseManager.profileCleanup(player.getName());
                        }

                    }
                    else {
                        if (LeaderboardManager.removeFlatFileUser(player.getName())) {
                            DatabaseManager.profileCleanup(player.getName());
                        }
                    }
                }
                return;
            }
            case SET: {
                if(qty >= 0 && !skill.equals("")) {
                    if(type == Type.LEVELS) {
                        ExperienceAPI.setLevel(player, skill, (int) qty);
                    } else if(type == Type.XP) {
                        ExperienceAPI.setXP(player, skill, (int) qty);
                    }
                } else if(type == Type.XPRATE) {
                    Config.getInstance().setExperienceGainsGlobalMultiplier(qty);
                } else if(type == Type.LEADER && !party.equals("")) {
                    PartyAPI.setPartyLeader(party, player.getName());
                } else if(type == Type.HARDCORE) {
                    boolean isEnabled = Config.getInstance().getHardcoreEnabled();
                    if(state == State.TOGGLE) {
                        Config.getInstance().setHardcoreEnabled(!isEnabled);
                    } else if(state == State.TRUE) {
                        Config.getInstance().setHardcoreEnabled(true);
                    } else if(state == State.FALSE) {
                        Config.getInstance().setHardcoreEnabled(false);
                    }
                }else if(type == Type.VAMPIRISM) {
                    boolean isEnabled = Config.getInstance().getHardcoreVampirismEnabled();
                    if(state == State.TOGGLE) {
                        Config.getInstance().setHardcoreVampirismEnabled(!isEnabled);
                    } else if(state == State.TRUE) {
                        Config.getInstance().setHardcoreVampirismEnabled(true);
                    } else if(state == State.FALSE) {
                        Config.getInstance().setHardcoreVampirismEnabled(false);
                    }
                }
                return;
            }
        }
    }
}