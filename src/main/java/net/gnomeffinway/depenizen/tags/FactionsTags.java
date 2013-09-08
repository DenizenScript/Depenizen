package net.gnomeffinway.depenizen.tags;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.dFaction;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;

public class FactionsTags implements Listener {
    
    public FactionsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void factionsTags(ReplaceableTagEvent event) {
    	
		// Build a new attribute out of the raw_tag supplied in the script to be fulfilled
		Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());
		
        /////////////////////
        //   PLAYER TAGS
        /////////////////
		
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
    		
    		if (attribute.startsWith("factions")) {
                if (player.hasFaction()) {
    		        if (attribute.startsWith("role")) {
    		            if (player.getRole() != null)
    		                event.setReplaced(new Element(player.getRole().toString()).getAttribute(attribute.fulfill(2)));
    		            else
    		                event.setReplaced(new Element("null").getAttribute(attribute.fulfill(2)));
    		        }
    		        
    		        else if (attribute.startsWith("title"))
    		            if (player.hasTitle())
    		                event.setReplaced(new Element(player.getTitle()).getAttribute(attribute.fulfill(2)));
    		            else
                            event.setReplaced(new Element("null").getAttribute(attribute.fulfill(2)));
    		    }
                
                if (attribute.startsWith("power"))
                    event.setReplaced(new Element(player.getPower()).getAttribute(attribute.fulfill(2)));
    		}
    		
    		else if (attribute.startsWith("faction")) {
            	event.setReplaced(new dFaction(player.getFaction()).getAttribute(attribute.fulfill(1)));
            }
    	}
    	
        /////////////////////
        //   FACTION TAGS
        /////////////////
    	
    	else if (event.matches("faction")) {
    	    
    		for (FactionColl fc : FactionColls.get().getColls()) {
    		    for (Faction f : fc.getAll()) {
    		        if (f.getName().equalsIgnoreCase(attribute.getContext(1))) {
    	                event.setReplaced(new dFaction(f).getAttribute(attribute.fulfill(1)));
    		        }
    		    }
    		}
    		
        }
        
    }
    
}
