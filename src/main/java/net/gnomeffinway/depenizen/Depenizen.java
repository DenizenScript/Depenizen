package net.gnomeffinway.depenizen;

import me.limebyte.battlenight.core.BattleNight;
import net.aufdemrand.denizen.Denizen;
import net.gnomeffinway.depenizen.support.BattleNightSupport;
import net.gnomeffinway.depenizen.support.FactionsSupport;
import net.gnomeffinway.depenizen.support.McMMOSupport;
import net.gnomeffinway.depenizen.support.TownySupport;
import net.gnomeffinway.depenizen.support.VotifierSupport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.mcMMO;
import com.massivecraft.factions.Factions;
import com.palmergames.bukkit.towny.Towny;
import com.vexsoftware.votifier.Votifier;

public class Depenizen extends JavaPlugin{
    
    public static mcMMO mcmmo;
    public static Factions factions;
    public static BattleNight battlenight;
    public static Denizen denizen;
    public static Towny towny;
    public static Votifier votifier;
    
    public McMMOSupport mcmmoSupport;
    public BattleNightSupport battlenightSupport;
    public TownySupport townySupport;
    public FactionsSupport factionsSupport;
    public VotifierSupport votifierSupport;

    @Override
    public void onEnable() {         
        checkPlugins();
        
        getLogger().info("Finished loading " + getDescription().getFullName());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("depenizen")) {
            sender.sendMessage(ChatColor.UNDERLINE + "Depenizen");
            sender.sendMessage(ChatColor.GRAY + "Developers: " + ChatColor.AQUA + "Morphan1" + ChatColor.GRAY + " and GnomeffinWay");
            sender.sendMessage(ChatColor.GRAY + "Current version: "+ ChatColor.GOLD + this.getDescription().getVersion());
            return true;
        }
        return false;
    }

    public void checkPlugins() {         
        denizen=(Denizen) getServer().getPluginManager().getPlugin("Denizen");
        mcmmo=(mcMMO) getServer().getPluginManager().getPlugin("mcMMO");
        factions=(Factions) getServer().getPluginManager().getPlugin("Factions");
        towny=(Towny) getServer().getPluginManager().getPlugin("Towny");
        battlenight=(BattleNight) getServer().getPluginManager().getPlugin("BattleNight");
        votifier=(Votifier) getServer().getPluginManager().getPlugin("Votifier");
        
        if (denizen != null) {
            getServer().getLogger().info("[Depenizen] Denizen hooked");
        }
        else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            this.getPluginLoader().disablePlugin(this);
            return;
        }           
        
        if (mcmmo != null) {
            getServer().getLogger().info("[Depenizen] mcMMO hooked, enabling add-ons.");
            mcmmoSupport = new McMMOSupport(this);
            mcmmoSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] mcMMO not found, add-ons will not enable.");
        }
        if (towny != null) {
            getServer().getLogger().info("[Depenizen] Towny hooked, enabling add-ons.");
            townySupport = new TownySupport(this);
            townySupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] Towny not found, add-ons. will not add-ons.");
        }
        
        if (factions != null) {
            getServer().getLogger().info("[Depenizen] Factions hooked, enabling add-ons.");
            factionsSupport = new FactionsSupport(this);
            factionsSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] Factions not found, add-ons will not enable.");
        }
        
        if (battlenight != null) {
            getServer().getLogger().info("[Depenizen] BattleNight hooked, enabling add-ons.");
            battlenightSupport = new BattleNightSupport(this);
            battlenightSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] BattleNight not found, add-ons will not enable.");
        }
        if (votifier != null) {
            getServer().getLogger().info("[Depenizen] Votifier hooked, enabling add-ons.");
            votifierSupport = new VotifierSupport(this);
            votifierSupport.register();
        }
        else {
            getServer().getLogger().info("[Depenizen] Votifier not found, add-ons will not enable.");
        }
        
    }

}
