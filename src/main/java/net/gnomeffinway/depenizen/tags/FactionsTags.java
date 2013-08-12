package net.gnomeffinway.depenizen.tags;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.FFlag;
import com.massivecraft.mcore.money.Money;

public class FactionsTags implements Listener {
    
    public FactionsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void factionsTags(ReplaceableTagEvent event) {
    	
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

    		UPlayer player = UPlayer.get(p.getPlayerEntity());
    		attribute = attribute.fulfill(1);
    		
            if (attribute.startsWith("faction")) {
            	
            	attribute = attribute.fulfill(1);
            	
                if (player.hasFaction()) {
                	
                    if (attribute.startsWith("role"))
                        if (player.getRole() != null)
                            replaced = new Element(player.getRole().name())
                    				.getAttribute(attribute.fulfill(1));
                    
                    else if (attribute.startsWith("title"))
                        if (player.getTitle() != null)
                            replaced = new Element(player.getTitle())
                        			.getAttribute(attribute.fulfill(1));
                    
                    else 
                    	replaced = new Element(player.getFaction().getName())
                        		.getAttribute(attribute);
                    
                } 
                
                else if (attribute.startsWith("power"))
                        replaced = new Element(player.getPower())
                    			.getAttribute(attribute.fulfill(1));
                    
            }
    	}
    	
    	else if (event.matches("faction")) {
    		
    		Faction faction = null;
    		
    		for (FactionColl fc : FactionColls.get().getColls())
    		    for (Faction f : fc.getAll())
    		        if (f.getComparisonName().equalsIgnoreCase(attribute.getContext(1))) {
    		        	faction = f;
    		        	break;
    		        }
            
            if (faction != null) {
                
            	if (attribute.startsWith("balance"))
                    replaced = new Element(Money.get(faction))
                    		.getAttribute(attribute.fulfill(1));

                else if (attribute.startsWith("home"))
                    replaced = new dLocation(faction.getHome().asBukkitBlock().getLocation())
                    		.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("isopen") || attribute.startsWith("is_open"))
                    replaced = new Element(faction.isOpen())
                    		.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("ispeaceful") || attribute.startsWith("is_peaceful"))
                    replaced = new Element(faction.getFlag(FFlag.PEACEFUL))
                    		.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("ispermanent") || attribute.startsWith("is_permanent"))
                    replaced = new Element(faction.getFlag(FFlag.PERMANENT))
            				.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("leader"))
                    replaced = new dPlayer(faction.getLeader().getPlayer())
            				.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
                    replaced = new Element(faction.getUPlayers().size())
            				.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("power"))
                    replaced = new Element(faction.getPower())
            				.getAttribute(attribute.fulfill(1));
                
                else if (attribute.startsWith("relation")) {
                	
                	Faction to = null;
                	
                	for (FactionColl fc : FactionColls.get().getColls())
            		    for (Faction f : fc.getAll())
            		        if (f.getComparisonName().equalsIgnoreCase(attribute.getContext(1))) {
            		        	to = f;
            		        	break;
            		        }
                	
                    if(to != null) 
                        replaced = new Element(faction.getRelationTo(to).name())
                    			.getAttribute(attribute.fulfill(1));
                
                }
                
                else if (attribute.startsWith("size"))
                    replaced = new Element(faction.getLandCount())
                    		.getAttribute(attribute.fulfill(1));
                
            }
            
        }
    	
    	if (replaced != null)
    		event.setReplaced(replaced);
        
    }
    
}
