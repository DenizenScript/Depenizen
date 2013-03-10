package net.gnomeffinway.depenizen.towny;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;
import net.gnomeffinway.depenizen.Depenizen;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

public class TownyTags implements Listener{
    
    public TownyTags(Depenizen depenizen) {
        depenizen.getServer().getPluginManager().registerEvents(this, depenizen);
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler
    public void townyTags(ReplaceableTagEvent event) throws NotRegisteredException {

        Player p = event.getPlayer();
        String nameContext = event.getNameContext() != null ? event.getNameContext().toUpperCase() : "";
        String type = event.getType() != null ? event.getType().toUpperCase() : "";
        String typeContext = event.getTypeContext() != null ? event.getTypeContext().toUpperCase() : "";
        String subType = event.getSubType() != null ? event.getSubType().toUpperCase() : "";
        String subTypeContext = event.getSubTypeContext() != null ? event.getSubTypeContext().toUpperCase() : "";

        Resident resident = null;
        Town town = null;
        Nation nation = null;
        
        if(event.matches("PLAYER")) {
            if (type.equals("TOWN")) {
                resident = Depenizen.towny.getTownyUniverse().getResident(p.getName());
                if(resident.hasTown()) {
                    if(subType.equals("ISMAYOR")) {
                        event.setReplaced(String.valueOf(resident.isMayor()));
                    } else if(subType.equals("RANK")) {
                        if(resident.getTownRanks().size() != 0) {
                            event.setReplaced(String.valueOf(resident.getTownRanks().get(0)));
                        } else {
                            event.setReplaced("none");
                        }
                    } else if(subType.equals("SURNAME")) {
                        if(resident.hasSurname()) {
                            event.setReplaced(String.valueOf(resident.getSurname()));
                        } else {
                            event.setReplaced("none");
                        }
                    } else if(subType.equals("TITLE")) {
                        if(resident.hasTitle()) {
                            event.setReplaced(String.valueOf(resident.getTitle()));
                        } else {
                            event.setReplaced("none");
                        }
                    } else {
                        event.setReplaced(resident.getTown().getName());
                    }
                } else {
                    event.setReplaced("none");
                }
            }
            if (type.equals("NATION")) {
                resident = Depenizen.towny.getTownyUniverse().getResident(p.getName());
                if(resident.hasNation()) {
                    if(subType.equals("ISKING")) {
                        event.setReplaced(String.valueOf(resident.isKing()));
                    } else if(subType.equals("SURNAME")) {
                        if(resident.hasSurname()) {
                            event.setReplaced(String.valueOf(resident.getSurname()));
                        } else {
                            event.setReplaced("none");
                        }
                    } else if(subType.equals("RANK")) {
                        if(resident.getNationRanks().size() != 0) {
                            event.setReplaced(String.valueOf(resident.getNationRanks().get(0)));
                        } else {
                            event.setReplaced("none");
                        }
                    } else {
                        event.setReplaced(resident.getTown().getName());
                    }
                } else {
                    event.setReplaced("none");
                }
            }
        } else if (event.matches("TOWN")) {
            try {
                town = Depenizen.towny.getTownyUniverse().getTown(nameContext);
            } catch(NotRegisteredException e) {
                return;
            }
            if (type.equals("BALANCE")) {
                try {
                    event.setReplaced(String.valueOf(town.getHoldingBalance()));
                } catch (EconomyException e) {
                    return;
                }
            } else if (type.equals("BOARD")) {
                if(town.getTownBoard() != null) {
                    event.setReplaced(String.valueOf(town.getTownBoard()));
                } else {
                    event.setReplaced("none");
                }
            } else if (type.equals("ISOPEN")) {
                event.setReplaced(String.valueOf(town.isOpen()));
            } else if (type.equals("ISPUBLIC")) {
                event.setReplaced(String.valueOf(town.isPublic()));
            } else if (type.equals("MAYOR")) {
                event.setReplaced(String.valueOf(town.getMayor().getName()));
            } else if (type.equals("NATION")) {
                if(town.hasNation()) {
                    event.setReplaced(String.valueOf(town.getNation()));
                } else {
                    event.setReplaced("none");
                }
            } else if (type.equals("PLAYERCOUNT")) {
                event.setReplaced(String.valueOf(town.getNumResidents()));
            } else if (type.equals("SIZE")) {
                event.setReplaced(String.valueOf(town.getPurchasedBlocks()));
            } else if (type.equals("SPAWN")) {
                Location spawn;
                try {
                    spawn = town.getSpawn();
                } catch (TownyException e) {
                    event.setReplaced("none");
                    return;
                }
                event.setReplaced(spawn.getBlockX() + "," + spawn.getBlockY() + "," + spawn.getBlockZ() + "," + spawn.getWorld().getName());
            } else if (type.equals("TAG")) {
                if(town.hasTag()) {
                    event.setReplaced(String.valueOf(town.getTag()));
                } else {
                    event.setReplaced("none");
                }
            } else if (type.equals("TAXES")) {
                event.setReplaced(String.valueOf(town.getTaxes()));
            }
        } else if (event.matches("NATION")) {
            try {
                nation = Depenizen.towny.getTownyUniverse().getNation(nameContext);
            } catch(NotRegisteredException e) {
                return;
            }
            if (type.equals("BALANCE")) {
                try {
                    event.setReplaced(String.valueOf(nation.getHoldingBalance()));
                } catch (EconomyException e) {
                    return;
                }
            } else if (type.equals("CAPITAL")) {
                if(nation.hasCapital()) {
                    event.setReplaced(String.valueOf(nation.getCapital().getName()));
                } else {
                    event.setReplaced("none");
                }
            } else if (type.equals("KING")) {
                event.setReplaced(String.valueOf(nation.getCapital().getMayor().getName()));
            } else if (type.equals("ISNEUTRAL")) {
                event.setReplaced(String.valueOf(nation.isNeutral()));
            } else if (type.equals("PLAYERCOUNT")) {
                event.setReplaced(String.valueOf(nation.getNumResidents()));
            } else if (type.equals("RELATION")) {
                Nation to;
                try {
                    to = Depenizen.towny.getTownyUniverse().getNation(typeContext);
                } catch(NotRegisteredException e) {
                    return;
                }                
                if(nation.hasAlly(to)) {
                    event.setReplaced("allies");
                } else if(nation.hasEnemy(to)) {
                    event.setReplaced("enemies");
                } else {
                    event.setReplaced(String.valueOf("neutral"));
                }
            } else if (type.equals("TAG")) {
                if(nation.hasTag()) {
                    event.setReplaced(String.valueOf(nation.getTag()));
                } else {
                    event.setReplaced("none");
                }
            } else if (type.equals("TAXES")) {
                event.setReplaced(String.valueOf(nation.getTaxes()));
            } else if (type.equals("TOWNCOUNT")) {
                event.setReplaced(String.valueOf(nation.getNumTowns()));
            }
        }
    }
}
