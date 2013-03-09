package net.gnomeffinway.depenizen.battlenight;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BattleNightTags implements Listener {
    
    public BattleNightTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void battlenightTags(ReplaceableTagEvent event) {

        // These tags require a player.
        if (!event.matches("PLAYER")) return;

        Player p = event.getPlayer();
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
    }
}