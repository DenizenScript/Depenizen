package net.gnomeffinway.depenizen.ancientrpg;

import net.aufdemrand.denizen.utilities.debugging.dB;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.ancientshores.AncientRPG.API.ARPGEntityDamageByEntityEvent;
import com.ancientshores.AncientRPG.API.ARPGEntityDamageEvent;

public class AncientRPGListeners implements Listener {
    
    public AncientRPGListeners() {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAncientEntityDamage(ARPGEntityDamageEvent e) {
        if(e.getEntity().hasMetadata("NPC")) {
            dB.echoDebug("Cancelling AncientRPG entity damage event on NPC");
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAncientEntityDamagebyEntity(ARPGEntityDamageByEntityEvent e) {
        if(e.getEntity().hasMetadata("NPC")) {
            dB.echoDebug("Cancelling AncientRPG entity damage event on NPC by "+e.getEntity().getEntityId());
            e.setCancelled(true);
        }
    }
    
}
