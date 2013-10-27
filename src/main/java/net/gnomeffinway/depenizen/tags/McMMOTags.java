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
import com.gmail.nossr50.datatypes.skills.SkillType;
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

                // <--[tag]
                // @attribute <p@player.mcmmo.level[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the player's level in a skill. If no skill is specified,
                // this returns the player's overall level.
                // @plugin mcMMO
                // -->
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

                // <--[tag]
                // @attribute <p@player.mcmmo.party>
                // @returns Element
                // @description
                // Returns the name of the player's party.
                // @plugin mcMMO
                // -->
                else if (attribute.startsWith("party"))
                    replaced = new Element(PartyAPI.getPartyName(p.getPlayerEntity()))
    						.getAttribute(attribute.fulfill(1));

                // -->
                else if (attribute.startsWith("xp")) {
                	
                	String skill = attribute.getContext(1);
                	attribute = attribute.fulfill(1);

                    // <--[tag]
                    // @attribute <p@player.mcmmo.to_next_level[<skill>]>
                    // @returns Element(Integer)
                    // @description
                    // Returns the amount of experience a player has left to level up
                	// in a skill.
                    // @plugin mcMMO
                	if (attribute.startsWith("tonextlevel") || attribute.startsWith("to_next_level")) {
                        if (p.isOnline()) {
                        	replaced = new Element(ExperienceAPI.getXPToNextLevel(p.getPlayerEntity(), skill))
                        			.getAttribute(attribute.fulfill(1));
                        }
                        else {
                        	replaced = new Element(ExperienceAPI.getOfflineXPToNextLevel(p.getName(), skill))
									.getAttribute(attribute.fulfill(1));
                        }
                    }

                    // <--[tag]
                    // @attribute <p@player.mcmmo.xp[<skill>].level>
                    // @returns Element(Integer)
                    // @description
                    // Returns the player's experience level in a skill.
                    // @plugin mcMMO
                	else if (attribute.startsWith("level")) {
                	    if (p.isOnline()) {
                	        replaced = new Element(ExperienceAPI.getLevel(p.getPlayerEntity(), skill))
                	                .getAttribute(attribute.fulfill(1));
                	    }
                	    else {
                	        replaced = new Element(ExperienceAPI.getLevelOffline(p.getName(), skill))
                	                .getAttribute(attribute.fulfill(1));
                	    }
                	}

                    // <--[tag]
                    // @attribute <p@player.mcmmo.xp[<skill>]>
                    // @returns Element(Integer)
                    // @description
                    // Returns the player's amount of experience in a skill.
                    // @plugin mcMMO
                	else if (p.isOnline()) {
                    	replaced = new Element(ExperienceAPI.getXP(p.getPlayerEntity(), skill))
								.getAttribute(attribute.fulfill(0));
                    }
                    else {
                    	replaced = new Element(ExperienceAPI.getOfflineXP(p.getName(), skill))
								.getAttribute(attribute.fulfill(0));
                    }
                	
                    
                    
                }

                // <--[tag]
                // @attribute <p@player.mcmmo.rank[<skill>]>
                // @returns Element(Integer)
                // @description
                // Returns the player's current rank in a skill. If no skill is specified,
                // this returns the player's overall rank.
                // @plugin mcMMO
                else if (attribute.startsWith("rank")) {
                    if (!attribute.hasContext(1)) {
                        replaced = new Element(ExperienceAPI.getPlayerRankOverall(p.getName()))
                             .getAttribute(attribute.fulfill(1));
                    }
                    else {
                        if (SkillType.getSkill(attribute.getContext(1)) != null)
                            replaced = new Element(ExperienceAPI.getPlayerRankSkill(p.getName(), attribute.getContext(1)))
                                    .getAttribute(attribute.fulfill(1));
                        else
                            dB.echoError("Skill type '" + attribute.getContext(1) + "' does not exist!");
                    }
                }
                
            }
                
        } 

    	else if (event.matches("party")) {
    		
    		if (!attribute.hasContext(1) || PartyManager.getParty(attribute.getContext(1)) == null) 
    			return;
    		
    		Party party = PartyManager.getParty(attribute.getContext(1));
    		attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <party[<party>].leader>
            // @returns dPlayer
            // @description
            // Returns the leader of the party.
            // @plugin mcMMO
            if (attribute.startsWith("leader"))
            	replaced = dPlayer.valueOf(party.getLeader())
            			.getAttribute(attribute.fulfill(1));

            // <--[tag]
            // @attribute <party[<party>].player_count>
            // @returns Element(Integer)
            // @description
            // Returns the number of players in the party.
            // @plugin mcMMO
            else if(attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            	replaced = new Element(party.getMembers().size())
            			.getAttribute(attribute.fulfill(1));
            
        }
    	
    	if (replaced != null)
    		event.setReplaced(replaced);
        
    }

    
}
