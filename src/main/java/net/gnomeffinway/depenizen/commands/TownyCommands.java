package net.gnomeffinway.depenizen.commands;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dLocation;

public class TownyCommands extends AbstractCommand {
    //TODO: CLASS ?
    private enum Action {ADD, REMOVE, SET}
    private enum State {TRUE, FALSE, TOGGLE} 
    private enum Type {RESIDENT, MONEY, TOWN, NATION, TOWNBLOCK, BONUS, PLOT, OUTPOST, NAME, 
        CAPITAL, OPEN, PUBLIC, MAYOR, SURNAME, TITLE, RANK, BOARD, WAR, MAXSIZE, TAXES, TAG, SPAWN, PERM, RELATION} 

    public TownyCommands() {
        
    }
    
    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        
        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {
        	
            if (!scriptEntry.hasObject("action")
            		&& arg.matchesEnum(Action.values()))
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            
            else if (!scriptEntry.hasObject("state")
            		&& arg.matchesEnum(State.values()))
                scriptEntry.addObject("state", State.valueOf(arg.getValue()));
            
            else if (!scriptEntry.hasObject("town")
            		&& arg.matchesPrefix("town"))
                scriptEntry.addObject("town", arg.asElement());
            
            else if (!scriptEntry.hasObject("nation")
            		&& arg.matchesPrefix("nation, name"))
                scriptEntry.addObject("nation", arg.asElement());
            
            else if (!scriptEntry.hasObject("location")
            		&& arg.matchesArgumentType(dLocation.class))
            	scriptEntry.addObject("location", arg.asType(dLocation.class));
            
            else if (!scriptEntry.hasObject("qty")
            		&& arg.matchesPrefix("qty, q, quantity")
            		&& arg.matchesPrimitive(aH.PrimitiveType.Double))
                scriptEntry.addObject("qty", arg.asElement());
            
            else if (!scriptEntry.hasObject("type")
            		&& arg.matchesEnum(Type.values()))
                scriptEntry.addObject("type", Type.valueOf(arg.getValue()));
            
        }
        
        scriptEntry.defaultObject("town", new Element("")).defaultObject("nation", "")
        		.defaultObject("state", State.TOGGLE).defaultObject("qty", new Element(-1));
        
    }
    
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        // Get objects
        /*
        Action action = (Action) scriptEntry.getObject("action");
        Player tempPlayer = (Player) scriptEntry.getObject("player");
        Player player = tempPlayer != null ? tempPlayer: scriptEntry.getPlayer();
        State state = State.valueOf(String.valueOf(scriptEntry.getObject("state")));
        double qty = Double.valueOf(String.valueOf(scriptEntry.getObject("qty")));
        String town = String.valueOf(scriptEntry.getObject("town"));
        Location location = aH.getLocationFrom(String.valueOf(scriptEntry.getObject("location")));
        String nation = String.valueOf(scriptEntry.getObject("nation"));
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
                    */

    }
}
