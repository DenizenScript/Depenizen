package net.gnomeffinway.depenizen.tags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.party.PartyManager;

import net.gnomeffinway.depenizen.Depenizen;

public class McMMOTags implements Listener {
    
    public McMMOTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void mcmmoTags(ReplaceableTagEvent event) {        
        
    	// Build a new attribute out of the raw_tag supplied in the script to be fulfilled
    	Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());
    			
    	// Set a string to use for replacing the tag
    	String replaced = null;
    			
    	if (event.matches("player, pl")) {
    	    		
    	    // PlayerTags require a... dPlayer!
    	    dPlayer p = event.getPlayer();

    	    // Player tag may specify a new player in the <player[context]...> portion of the tag.
    	    if (attribute.hasContext(1))
    	    	// Check if this is a valid player and update the dPlayer object reference.
    	    	if (dPlayer.matches(attribute.getContext(1)))
    	    		p = dPlayer.valueOf(attribute.getContext(1));
    	    	else {
    	    		dB.echoDebug("Could not match '"
    	    				+ attribute.getContext(1) + "' to a valid player!");
    	    		return;
    	    		}

    	    if (p == null || !p.isValid()) {
    	    	dB.echoDebug("Invalid or missing player for tag <" + event.raw_tag + ">!");
    	    	event.setReplaced("null");
    	    	return;
    	    }
    	    
    	    attribute = attribute.fulfill(1);
    	    		
            if (attribute.startsWith("mcmmo")) {
            	
            	attribute = attribute.fulfill(1);
            	
                if (attribute.startsWith("level")) {
                    if(!attribute.hasContext(1)) {
                        if (p.isOnline()) {
                            replaced = new Element(ExperienceAPI.getPowerLevel(p.getPlayerEntity()))
                        			.getAttribute(attribute.fulfill(1));
                        }
                        else {
                            replaced = new Element(ExperienceAPI.getPowerLevelOffline(p.getPlayerEntity().getName()))
                        			.getAttribute(attribute.fulfill(1));
                        }
                    } 
                    else {
                        if (p.isOnline()) {
                            replaced = new Element(ExperienceAPI.getLevel(p.getPlayerEntity(), attribute.getContext(1)))
                        			.getAttribute(attribute.fulfill(1));
                        }
                        else {
                            replaced = new Element(ExperienceAPI.getLevelOffline(p.getName(), attribute.getContext(1)))
                        			.getAttribute(attribute.fulfill(1));
                        }
                    }
                } 
                
                else if (attribute.startsWith("party"))
                    if(PartyAPI.inParty(p.getPlayerEntity()))
                        replaced = new Element(PartyAPI.getPartyName(p.getPlayerEntity()))
    							.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("xp")) {
                	
                	attribute = attribute.fulfill(1);
                	
                	if (p.isOnline()) {
                    	replaced = new Element(ExperienceAPI.getXP(p.getPlayerEntity(), attribute.getContext(1)))
								.getAttribute(attribute.fulfill(1));
                    }
                    else {
                    	replaced = new Element(ExperienceAPI.getOfflineXP(p.getName(), attribute.getContext(1)))
								.getAttribute(attribute.fulfill(1));
                    }
                	
                    if (attribute.startsWith("tonextlevel") || attribute.startsWith("to_next_level")) {
                        if (p.isOnline()) {
                        	replaced = new Element(ExperienceAPI.getXPToNextLevel(p.getPlayerEntity(), attribute.getContext(1)))
                        			.getAttribute(attribute.fulfill(0));
                        }
                        else {
                        	replaced = new Element(ExperienceAPI.getOfflineXPToNextLevel(p.getName(), attribute.getContext(1)))
									.getAttribute(attribute.fulfill(0));
                        }
                    }
                    
                }
                
             // Ranks seem to have disappeared...
                
                /*
                else if (attribute.startsWith("rank")) {
                    if(!attribute.hasContext(1))
                    	replaced = new Element(LeaderboardManager.getPlayerRank(p.getName())[0])
								.getAttribute(attribute.fulfill(1));
                    else
                    	replaced = new Element(LeaderboardManager.getPlayerRank(p.getName(), SkillType.getSkill(attribute.getContext(1)))[0])
								.getAttribute(attribute.fulfill(1));
                }
                */
                
            }
        } 
    	
    	else if (event.matches("party")) {
    		
    		if (!attribute.hasContext(1) || PartyManager.getParty(attribute.getContext(1)) == null) 
    			return;
    		
    		Party party = PartyManager.getParty(attribute.getContext(1));
    		attribute = attribute.fulfill(1);
    		
            if (attribute.startsWith("leader"))
            	replaced = dPlayer.valueOf(party.getLeader())
            			.getAttribute(attribute.fulfill(1));
            
            else if(attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            	replaced = new Element(party.getMembers().size())
            			.getAttribute(attribute.fulfill(1));
            
        }
    	
    	if (replaced != null)
    		event.setReplaced(replaced);
        
    }

    
}
