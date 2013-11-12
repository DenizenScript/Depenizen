package net.gnomeffinway.depenizen.commands;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.party.PartyManager;

public class McMMOCommands extends AbstractCommand {
    private enum Action {ADD, REMOVE, SET}
    private enum State {TRUE, FALSE, TOGGLE} 
    private enum Type {XP, LEVELS, TOGGLE, XPRATE, LEADER, VAMPIRISM, HARDCORE} 

    public McMMOCommands() {
        
    }
    
    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        
        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {
        	
            if (!scriptEntry.hasObject("action")
            		&& arg.matchesEnum(Action.values()))
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            
            else if (!scriptEntry.hasObject("state")
            		&& arg.matchesPrefix("state")
            		&& arg.matchesEnum(State.values()))
                scriptEntry.addObject("state", State.valueOf(arg.getValue()));
            
            else if (!scriptEntry.hasObject("party")
            		&& arg.matchesPrefix("party"))
                scriptEntry.addObject("party", arg.asElement());
            
            else if (!scriptEntry.hasObject("skill")
            		&& arg.matchesPrefix("skill"))
            	scriptEntry.addObject("skill", arg.asElement());
            
            else if (!scriptEntry.hasObject("qty, q, quantity")
            		&& arg.matchesPrefix("qty")
            		&& arg.matchesPrimitive(aH.PrimitiveType.Double))
            	scriptEntry.addObject("qty", arg.asElement());
            
            else if (!scriptEntry.hasObject("type")
            		&& arg.matchesEnum(Type.values()))
                scriptEntry.addObject("type", Type.valueOf(arg.getValue()));
            
        }
        
        scriptEntry.defaultObject("state", State.TOGGLE).defaultObject("qty", new Element(-1)).defaultObject("skill", new Element("")).defaultObject("party", new Element(""));
        
    }
    
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        // Get objects
        Action action = (Action) scriptEntry.getObject("action");
        State state = (State) scriptEntry.getObject("state");
        Type type = (Type) scriptEntry.getObject("type");
        Player player = scriptEntry.getPlayer().getPlayerEntity();
        double qty = scriptEntry.getElement("qty").asDouble();
        String party = scriptEntry.getElement("party").asString();
        String skill = scriptEntry.getElement("skill").asString();

        // Report to dB
        dB.report(scriptEntry, getName(),
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
            	
                if (qty >= 0 && !skill.equals("")) {
                    if (type == Type.LEVELS) {
                        if (player.isOnline()) {
                            ExperienceAPI.addLevel(player, skill, (int) qty);
                        }
                        else {
                            ExperienceAPI.addLevelOffline(player.getName(), skill, (int) qty);
                        }
                    }
                    
                    else if (type == Type.XP) {
                    	if (player.isOnline()) {
                            ExperienceAPI.addRawXP(player, skill, (float) qty);
                        } else {
                            ExperienceAPI.addRawXPOffline(player.getName(), skill, (float) qty);
                        }
                    }
                } 
                
                else if (PartyManager.isParty(party)) {
                    PartyAPI.addToParty(player, party);
                }
                return;
                
            }
            case REMOVE: {
            	
                if (qty >= 0 && skill != null) {
                    if (type == Type.LEVELS) {
                        if (player.isOnline())
                            ExperienceAPI.setLevel(player, skill, ExperienceAPI.getLevel(player, skill) - (int) qty);
                        else
                            ExperienceAPI.setLevelOffline(player.getName(), skill, ExperienceAPI.getLevel(player, skill) - (int) qty);
                    }
                    
                    else if (type == Type.XP) {
                        if(player.isOnline())
                            ExperienceAPI.removeXP(player, skill, (int) qty);
                        else
                            ExperienceAPI.removeXPOffline(player.getName(), skill, (int) qty);
                    }
                } 
                
                else if (PartyManager.isParty(party)) {
                	if(PartyAPI.getPartyLeader(party).equals(player.getName()))
                        PartyManager.disbandParty(PartyManager.getParty(party));
                    
                    else
                        PartyAPI.removeFromParty(player);
                } 
                
                // Don't know what changed here, will figure it out soon
                
                /*
                else if (player != null) {
                    if (Config.getInstance().getUseMySQL()) {
                        String tablePrefix = Config.getInstance().getMySQLTablePrefix();
                        if (DatabaseUpdateType.update("DELETE FROM " + tablePrefix + "users WHERE " + tablePrefix + "users.user = '" + player.getName() + "'") != 0)
                            Misc.profileCleanup(player.getName());
                    }
                    else if (LeaderboardManager.removeFlatFileUser(player.getName()))
                            Misc.profileCleanup(player.getName());
                }
                
                return;
                */
                
            }
            case SET: {
            	
                if (qty >= 0 && !skill.equals("")) {
                    if (type == Type.LEVELS) {
                        if(player.isOnline())
                            ExperienceAPI.setLevel(player, skill, (int) qty);
                        else
                            ExperienceAPI.setLevelOffline(player.getName(), skill, (int) qty);
                    } 
                    else if(type == Type.XP) {
                        if(player.isOnline())
                            ExperienceAPI.setXP(player, skill, (int) qty);
                        else
                            ExperienceAPI.setXPOffline(player.getName(), skill, (int) qty);
                    }
                }
                else if(type == Type.LEADER && !party.equals(""))
                    PartyAPI.setPartyLeader(party, player.getName());
                /*
                else if(type == Type.XPRATE)
                    Config.getInstance().setExperienceGainsGlobalMultiplier(qty);
                else if(type == Type.HARDCORE) {
                    boolean isEnabled = Config.getInstance().getHardcoreEnabled();
                    
                    if(state == State.TOGGLE)
                        Config.getInstance().setHardcoreEnabled(!isEnabled);
                    else if(state == State.TRUE)
                        Config.getInstance().setHardcoreEnabled(true);
                    else if(state == State.FALSE)
                        Config.getInstance().setHardcoreEnabled(false);
                    
                }
                else if(type == Type.VAMPIRISM) {
                    boolean isEnabled = Config.getInstance().getHardcoreVampirismEnabled();
                    
                    if(state == State.TOGGLE)
                        Config.getInstance().setHardcoreVampirismEnabled(!isEnabled);
                    else if(state == State.TRUE)
                        Config.getInstance().setHardcoreVampirismEnabled(true);
                    else if(state == State.FALSE)
                        Config.getInstance().setHardcoreVampirismEnabled(false);
                    
                }
                */
                return;
            }
        }
    }
}