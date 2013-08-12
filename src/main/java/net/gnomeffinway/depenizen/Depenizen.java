package net.gnomeffinway.depenizen;

import java.io.IOException;

import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.tags.ObjectFetcher;
import net.gnomeffinway.depenizen.support.BattleNightSupport;
import net.gnomeffinway.depenizen.support.FactionsSupport;
import net.gnomeffinway.depenizen.support.McMMOSupport;
import net.gnomeffinway.depenizen.support.TownySupport;

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
        
        try {
			ObjectFetcher._initialize();
		} 
        catch (ClassNotFoundException e) {} 
        catch (IOException e) {}
        
    }

}
