package net.gnomeffinway.depenizen.factions;


import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;

public class FactionsTags implements Listener {
    
    public FactionsTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @EventHandler
    public void factionsTags(ReplaceableTagEvent event) {

        Player p = event.getPlayer();
        String nameContext = event.getNameContext() != null ? event.getNameContext().toUpperCase() : "";
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        String typeContext = event.getTypeContext() != null ? event.getTypeContext().toUpperCase() : "";
        String subType = event.getSubType() != null ? event.getSubType().toUpperCase() : "";
        String subTypeContext = event.getSubTypeContext() != null ? event.getSubTypeContext().toUpperCase() : "";

        Faction faction = null;
        
        if(event.matches("PLAYER")) {
            if (type.equals("FACTION")) {
                if(FPlayers.i.get(p).hasFaction()) {
                    if(subType.equals("ROLE")) {
                        if(FPlayers.i.get(p).getRole() == null)
                            event.setReplaced(String.valueOf(FPlayers.i.get(p).getRole()));
                        else
                            event.setReplaced("none");
                    } else if(subType.equals("TITLE")) {
                        if(FPlayers.i.get(p).getTitle() == null)
                            event.setReplaced(String.valueOf(FPlayers.i.get(p).getTitle()));
                        else
                            event.setReplaced("none");
                    } else {
                            event.setReplaced(String.valueOf(FPlayers.i.get(p).getFaction().getTag()));
                    }
                } else {
                    if(subType.equals("POWER")) {
                        event.setReplaced(String.valueOf(FPlayers.i.get(p).getPower()));
                    } else {
                    event.setReplaced("none");
                    }
                }
            }
        } else if (event.matches("FACTION")) {
            faction = Factions.i.getByTag(nameContext);
            if(faction != null) {
                if (type.equals("BALANCE")) {
                    event.setReplaced(String.valueOf(faction.money));
                } else if (type.equals("HOME")) {
                    Location home = faction.getHome();
                    event.setReplaced(home.getBlockX() + "," + home.getBlockY() + "," + home.getBlockZ() + "," + home.getWorld().getName());
                } else if (type.equals("ISOPEN")) {
                    event.setReplaced(String.valueOf(faction.getOpen()));
                } else if (type.equals("ISPEACEFUL")) {
                    event.setReplaced(String.valueOf(faction.isPeaceful()));
                } else if (type.equals("ISPERMANENT")) {
                    event.setReplaced(String.valueOf(faction.isPermanent()));
                } else if (type.equals("LEADER")) {
                    event.setReplaced(String.valueOf(faction.getFPlayerAdmin().getName()));
                } else if (type.equals("PLAYERCOUNT")) {
                    event.setReplaced(String.valueOf(faction.getFPlayers().size()));
                } else if (type.equals("POWER")) {
                    event.setReplaced(String.valueOf(faction.getPower()));
                } else if (type.equals("RELATION")) {
                    Faction to = Factions.i.get(typeContext);
                    if(to != null) {
                        event.setReplaced(String.valueOf(faction.getRelationTo(to)));
                    }
                } else if (type.equals("SIZE")) {
                    event.setReplaced(String.valueOf(faction.getLandRounded()));
                }
            }
        }
        
    }
    
}
