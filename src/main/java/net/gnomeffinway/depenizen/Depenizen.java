package net.gnomeffinway.depenizen;

import com.gmail.nossr50.mcMMO;
import com.herocraftonline.heroes.Heroes;
import com.massivecraft.factions.Factions;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.vexsoftware.votifier.Votifier;
import me.limebyte.battlenight.core.BattleNight;
import me.zford.jobs.bukkit.JobsPlugin;
import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.support.*;
import net.slipcor.pvparena.PVPArena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

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
    public static WorldEditPlugin worldedit;
    public static WorldGuardPlugin worldguard;

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
    public WorldEditSupport worldeditSupport;
    public WorldGuardSupport worldguardSupport;

    @Override
    public void onEnable() {
        checkPlugins();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("depenizen")) {
            sender.sendMessage(ChatColor.UNDERLINE + "Depenizen");
            sender.sendMessage(ChatColor.GRAY + "Developers: " + ChatColor.AQUA + "Morphan1" + ChatColor.GRAY + ", " + ChatColor.GREEN + "Jeebiss" + ChatColor.GRAY + ", and GnomeffinWay");
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
        dynmap = (DynmapAPI) getServer().getPluginManager().getPlugin("Dynmap");
        heroes = (Heroes) getServer().getPluginManager().getPlugin("Heroes");
        worldedit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        worldguard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (denizen != null) {
            dB.log("Denizen hooked");
        } else {
            getServer().getLogger().severe("[Depenizen] Denizen not found, disabling");
            getPluginLoader().disablePlugin(this);
            return;
        }

        if (mcmmo != null) {
            dB.log("mcMMO hooked, enabling add-ons.");
            mcmmoSupport = new McMMOSupport(this);
            mcmmoSupport.register();
        } else {
            dB.log("mcMMO not found, add-ons will not enable.");
        }

        if (towny != null) {
            dB.log("Towny hooked, enabling add-ons.");
            townySupport = new TownySupport(this);
            townySupport.register();
        } else {
            dB.log("Towny not found, add-ons. will not add-ons.");
        }

        if (factions != null) {
            dB.log("Factions hooked, enabling add-ons.");
            factionsSupport = new FactionsSupport(this);
            factionsSupport.register();
        } else {
            dB.log("Factions not found, add-ons will not enable.");
        }

        if (battlenight != null) {
            dB.log("BattleNight hooked, enabling add-ons.");
            battlenightSupport = new BattleNightSupport(this);
            battlenightSupport.register();
        } else {
            dB.log("BattleNight not found, add-ons will not enable.");
        }

        if (votifier != null) {
            dB.log("Votifier hooked, enabling add-ons.");
            votifierSupport = new VotifierSupport(this);
            votifierSupport.register();
        } else {
            dB.log("Votifier not found, add-ons will not enable.");
        }

        if (bungeefier != null) {
            dB.log("Bungeefier hooked, enabling add-ons.");
            bungeefierSupport = new VotifierSupport(this);
            bungeefierSupport.register();
        } else {
            dB.log("Bungeefier not found, add-ons will not enable.");
        }

        if (jobs != null) {
            dB.log("Jobs hooked, enabling add-ons.");
            jobsSupport = new JobsSupport(this);
            jobsSupport.register();
        } else {
            dB.log("Jobs not found, add-ons will not enable.");
        }

        if (pvparena != null) {
            dB.log("PvP Arena hooked, enabling add-ons.");
            pvparenaSupport = new PVPArenaSupport(this);
            pvparenaSupport.register();
        } else {
            dB.log("PvP Arena not found, add-ons will not enable.");
        }

        if (heroes != null) {
            dB.log("Heroes hooked, enabling add-ons.");
            heroesSupport = new HeroesSupport(this);
            heroesSupport.register();
        } else {
            dB.log("Heroes not found, add-ons will not enable.");
        }

        if (dynmap != null) {
            dB.log("Dynmap hooked, enabling add-ons.");
            dynmapSupport = new DynmapSupport(this);
            dynmapSupport.register();
        } else {
            dB.log("Dynmap not found, add-ons will not enable.");
        }

        if (worldedit != null) {
            dB.log("WorldEdit hooked, enabling add-ons.");
            worldeditSupport = new WorldEditSupport(this);
            worldeditSupport.register();
        } else {
            dB.log("WorldEdit not found, add-ons will not enable.");
        }

        if (worldguard != null) {
            dB.log("WorldGuard hooked, enabling add-ons.");
            worldguardSupport = new WorldGuardSupport(this);
            worldguardSupport.register();
        } else {
            dB.log("WorldGuard not found, add-ons will not enable.");
        }
    }

}
