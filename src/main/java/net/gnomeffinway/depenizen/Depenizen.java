package net.gnomeffinway.depenizen;

import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.Denizen;
import net.gnomeffinway.depenizen.battlenight.BattleNightSupport;
import net.gnomeffinway.depenizen.factions.FactionsSupport;
import net.gnomeffinway.depenizen.mcmmo.McMMOSupport;
import net.gnomeffinway.depenizen.towny.TownySupport;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.mcMMO;
import com.massivecraft.factions.Factions;
import com.palmergames.bukkit.towny.Towny;

public class Depenizen extends JavaPlugin{
    
    public static mcMMO mcmmo;
    public static Factions factions;
    public static BattleNight battlenight;
    public static Denizen denizen;
    public static Towny towny;
    
    public McMMOSupport mcmmoSupport;
    public BattleNightSupport battlenightSupport;
    public TownySupport townySupport;
    public FactionsSupport factionsSupport;

    @Override
    public void onEnable() {         
        checkPlugins();
        
        getLogger().info("Finished loading " + getDescription().getFullName());
    }

    @Override
    public void onDisable() {

    }

    public void checkPlugins() {         
        denizen=(Denizen) getServer().getPluginManager().getPlugin("Denizen");
        mcmmo=(mcMMO) getServer().getPluginManager().getPlugin("mcMMO");
        factions=(Factions) getServer().getPluginManager().getPlugin("Factions");
        towny=(Towny) getServer().getPluginManager().getPlugin("Towny");
        battlenight=(BattleNight) getServer().getPluginManager().getPlugin("BattleNight");
        
        if(denizen != null) {
            getServer().getLogger().info("[Depenizen] Denizen hooked");
        }
        else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            this.getPluginLoader().disablePlugin(this);
            return;
        }           
        
        if(mcmmo != null) {
            getServer().getLogger().info("[Depenizen] mcMMO hooked, enabling addons");
            mcmmoSupport = new McMMOSupport(this);
            mcmmoSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] mcMMO not found, addons will not enable");
        }
        
        if(towny != null) {
            getServer().getLogger().info("[Depenizen] Towny hooked, enabling addons");
            townySupport = new TownySupport(this);
            townySupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] Towny not found, addons will not addons");
        }
        
        if(factions != null) {
            getServer().getLogger().info("[Depenizen] Factions hooked, enabling addons");
            factionsSupport = new FactionsSupport(this);
            factionsSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] Factions not found, addons will not enable");
        }
        
        if(battlenight != null) {
            getServer().getLogger().info("[Depenizen] BattleNight hooked, enabling addons");
            battlenightSupport = new BattleNightSupport(this);
            battlenightSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] BattleNight not found, add-ons will not enable");
        }
    }

}
