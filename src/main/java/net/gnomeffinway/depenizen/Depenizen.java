package net.gnomeffinway.depenizen;

import me.limebyte.battlenight.core.BattleNight;
import me.zford.jobs.bukkit.JobsPlugin;
import net.aufdemrand.denizen.Denizen;
import net.gnomeffinway.depenizen.support.*;
import net.slipcor.pvparena.PVPArena;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import com.gmail.nossr50.mcMMO;
import com.herocraftonline.heroes.Heroes;
import com.massivecraft.factions.Factions;
import com.palmergames.bukkit.towny.Towny;
import com.vexsoftware.votifier.Votifier;

public class Depenizen extends JavaPlugin {

    public static mcMMO mcmmo;
    public static Factions factions;
    public static BattleNight battlenight;
    public static Denizen denizen;
    public static Towny towny;
    public static Votifier votifier;
    public static Votifier bungeefier;
    public static JobsPlugin jobs;
    public static PVPArena pvparena;
    public static Heroes heroes;
    public static DynmapAPI dynmap;

    public McMMOSupport mcmmoSupport;
    public BattleNightSupport battlenightSupport;
    public TownySupport townySupport;
    public FactionsSupport factionsSupport;
    public VotifierSupport votifierSupport;
    public VotifierSupport bungeefierSupport;
    public JobsSupport jobsSupport;
    public PVPArenaSupport pvparenaSupport;
    public HeroesSupport heroesSupport;
    public DynmapSupport dynmapSupport;

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
            sender.sendMessage(ChatColor.GRAY + "Developers: " + ChatColor.AQUA + "Morphan1" + ChatColor.GRAY + ", " + ChatColor.GREEN + "Jeebiss" + ChatColor.GRAY + " and GnomeffinWay");
            sender.sendMessage(ChatColor.GRAY + "Current version: "+ ChatColor.GOLD + this.getDescription().getVersion());
            return true;
        }
        return false;
    }

    public void checkPlugins() {
        denizen = (Denizen) getServer().getPluginManager().getPlugin("Denizen");
        mcmmo = (mcMMO) getServer().getPluginManager().getPlugin("mcMMO");
        factions = (Factions) getServer().getPluginManager().getPlugin("Factions");
        towny = (Towny) getServer().getPluginManager().getPlugin("Towny");
        battlenight = (BattleNight) getServer().getPluginManager().getPlugin("BattleNight");
        votifier = (Votifier) getServer().getPluginManager().getPlugin("Votifier");
        bungeefier = (Votifier) getServer().getPluginManager().getPlugin("Bungeefier");
        jobs = (JobsPlugin) getServer().getPluginManager().getPlugin("Jobs");
        pvparena = (PVPArena) getServer().getPluginManager().getPlugin("pvparena");
        dynmap = (DynmapAPI) getServer().getPluginManager().getPlugin("dynmap"); 

        if (denizen != null) {
            getServer().getLogger().info("[Depenizen] Denizen hooked");
        } else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        if (mcmmo != null) {
            getServer().getLogger().info("[Depenizen] mcMMO hooked, enabling add-ons.");
            mcmmoSupport = new McMMOSupport(this);
            mcmmoSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] mcMMO not found, add-ons will not enable.");
        }
        if (towny != null) {
            getServer().getLogger().info("[Depenizen] Towny hooked, enabling add-ons.");
            townySupport = new TownySupport(this);
            townySupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Towny not found, add-ons. will not add-ons.");
        }

        if (factions != null) {
            getServer().getLogger().info("[Depenizen] Factions hooked, enabling add-ons.");
            factionsSupport = new FactionsSupport(this);
            factionsSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Factions not found, add-ons will not enable.");
        }

        if (battlenight != null) {
            getServer().getLogger().info("[Depenizen] BattleNight hooked, enabling add-ons.");
            battlenightSupport = new BattleNightSupport(this);
            battlenightSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] BattleNight not found, add-ons will not enable.");
        }
        if (votifier != null) {
            getServer().getLogger().info("[Depenizen] Votifier hooked, enabling add-ons.");
            votifierSupport = new VotifierSupport(this);
            votifierSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Votifier not found, add-ons will not enable.");
        }
        if (bungeefier != null) {
            getServer().getLogger().info("[Depenizen] Bungeefier hooked, enabling add-ons.");
            bungeefierSupport = new VotifierSupport(this);
            bungeefierSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Bungeefier not found, add-ons will not enable.");
        }
        if (jobs != null) {
            getServer().getLogger().info("[Depenizen] Jobs hooked, enabling add-ons.");
            jobsSupport = new JobsSupport(this);
            jobsSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Jobs not found, add-ons will not enable.");
        }
        if (pvparena != null) {
            getServer().getLogger().info("[Depenizen] PvP Arena hooked, enabling add-ons.");
            pvparenaSupport = new PVPArenaSupport(this);
            pvparenaSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] PvP Arena not found, add-ons will not enable.");
        }
        if (heroes != null) {
            getServer().getLogger().info("[Depenizen] Heroes hooked, enabling add-ons.");
            heroesSupport = new HeroesSupport(this);
            heroesSupport.register();
        } else {
            getServer().getLogger().info("[Depenizen] Heroes not found, add-ons will not enable.");
        }
        if (dynmap != null) {
        	getServer().getLogger().info("[Depenizen] Dynmap hooked, enabling add-ons.");
        	dynmapSupport = new DynmapSupport(this);
        	dynmapSupport.register();
        }

    }

}
