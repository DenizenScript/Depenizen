package net.gnomeffinway.depenizen.mcmmo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import com.gmail.nossr50.api.ExperienceAPI;
import net.gnomeffinway.depenizen.Depenizen;

public class McMMOTags implements Listener {
    
    public McMMOTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void mcmmoTags(ReplaceableTagEvent event) {

        // These tags require a player.
        if (!event.matches("PLAYER")) return;

        Player p = event.getPlayer();
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        String subType = event.getSubType() != null ? event.getSubType().toUpperCase() : "";
        String subTypeContext = event.getSubTypeContext() != null ? event.getSubTypeContext().toUpperCase() : "";
        
        if (type.equals("MCMMO")) {
            if(Depenizen.mcmmo != null) {
                if (subType.equals("LEVEL")) {
                    try{
                        event.setReplaced(String.valueOf(ExperienceAPI.getLevel(p, subTypeContext)));
                    } catch(Exception e) {
                        dB.echoError("Could not replace tag!");
                    }
                }
            } else {
                dB.echoError("mcMMO not loaded! Have you installed the mcMMO plugin?");
            }
        }
    }

    
}
