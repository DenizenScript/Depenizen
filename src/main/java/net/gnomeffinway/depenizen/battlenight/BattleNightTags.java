package net.gnomeffinway.depenizen.battlenight;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.limebyte.battlenight.core.BattleNight;

public class BattleNightTags implements Listener {
    
    public BattleNightTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void battlenightTags(ReplaceableTagEvent event) {
        
        Player p = event.getPlayer();
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        String typeContext = event.getTypeContext() != null ? event.getTypeContext().toUpperCase() : "";
        String subType = event.getSubType() != null ? event.getSubType().toUpperCase() : "";
        String subTypeContext = event.getSubTypeContext() != null ? event.getSubTypeContext().toUpperCase() : "";
        String specifier = event.getSpecifier() != null ? event.getSpecifier().toUpperCase() : "";  

        if(event.matches("PLAYER")) {
            if (type.equals("BN")) {
                if (subType.equals("CLASS")) {
                    if(BattleNight.instance.getAPI().getPlayerClass(p) != null) {
                        event.setReplaced(String.valueOf(BattleNight.instance.getAPI().getPlayerClass(p).getName()));
                    } else {
                        event.setReplaced("none");
                    }
                } else if (subType.equals("INBATTLE")) {
                    event.setReplaced(String.valueOf(BattleNight.instance.getAPI().getBattle().containsPlayer(p)));
                } else if (subType.equals("TEAM")) {
                    event.setReplaced("none");
                }
            }
        } else if (event.matches("BATTLE")) {
            if (type.equals("ARENA")) {
                event.setReplaced(String.valueOf(BattleNight.instance.getAPI().getBattle().getArena().getName()));
            } else if (type.equals("INPROGRESS")) {
                event.setReplaced(String.valueOf(BattleNight.instance.getAPI().getBattle().isInProgress()));
            } else if (type.equals("TIMEREMAINING")) {
                event.setReplaced(String.valueOf(BattleNight.instance.getAPI().getBattle().getTimer().getTimeRemaining()));
            }
        }
        
    }
}