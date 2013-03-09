package net.gnomeffinway.depenizen.factions;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayers;

public class FactionsTags implements Listener {
    
    public FactionsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void factionsTags(ReplaceableTagEvent event) {

        // These tags require a player.
        if (!event.matches("PLAYER")) return;

        Player p = event.getPlayer();
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        
        if (type.equals("FACTION")) {
            if(Depenizen.factions != null) {
                if(FPlayers.i.get(p).hasFaction())
                    event.setReplaced(String.valueOf(FPlayers.i.get(p).getFaction().getTag()));
                else
                    event.setReplaced("none");
            } else {
                dB.echoError("Factions not loaded! Have you installed the Factions plugin?");
            }
        }
    }
}
