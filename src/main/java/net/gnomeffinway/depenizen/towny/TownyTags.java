package net.gnomeffinway.depenizen.towny;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;

public class TownyTags implements Listener{
    
    public TownyTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void townyTags(ReplaceableTagEvent event) throws NotRegisteredException {

        // These tags require a player.
        if (!event.matches("PLAYER")) return;

        Player p = event.getPlayer();
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        
        if (type.equals("TOWN")) {
            if(Depenizen.towny != null) {
                if(Depenizen.towny.getTownyUniverse().getResident(p.getName()).hasTown())
                    event.setReplaced(String.valueOf(Depenizen.towny.getTownyUniverse().getResident(p.getName()).getTown().getName()));
                else
                    event.setReplaced("none");
            } else {
                dB.echoError("Towny not loaded! Have you installed the Towny plugin?");
            }
        }
        
        if (type.equals("NATION")) {
            if(Depenizen.towny != null) {
                if(Depenizen.towny.getTownyUniverse().getResident(p.getName()).hasNation())
                    event.setReplaced(String.valueOf(Depenizen.towny.getTownyUniverse().getResident(p.getName()).getTown().getNation().getName()));
                else
                    event.setReplaced("none");
            } else {
                dB.echoError("Towny not loaded! Have you installed the Towny plugin?");
            }
        }
    }
}
