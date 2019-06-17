package com.denizenscript.depenizen.bukkit;

import com.denizenscript.depenizen.bukkit.bridges.*;
import com.denizenscript.depenizen.bukkit.utilities.BridgeLoadException;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Supplier;

public class Depenizen extends JavaPlugin {

    public static Depenizen instance;

    public HashMap<String, Supplier<Bridge>> allBridges = new HashMap<>();

    public HashMap<String, Bridge> loadedBridges = new HashMap<>();

    @Override
    public void onEnable() {
        dB.log("Depenizen loading...");
        instance = this;
        registerCoreBridges();
        dB.log("Depenizen loaded! " + loadedBridges.size() + " bridges loaded (of " + allBridges.size() + " available)");
    }

    public void loadBridge(String name, Supplier<Bridge> bridgeSupplier) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) {
            dB.log("<G>Depenizen will not load support for '" + name + "'.");
            return;
        }
        Bridge newBridge;
        try {
            newBridge = bridgeSupplier.get();
        }
        catch (Throwable ex) {
            dB.echoError("Cannot load Depenizen support for '" + name + "': fundamental loading error: " + ex.getMessage());
            return;
        }
        newBridge.name = name;
        newBridge.plugin = plugin;
        try {
            newBridge.init();
        }
        catch (BridgeLoadException ex) {
            dB.echoError("Cannot load Depenizen support for '" + name + "': " + ex.getMessage());
            return;
        }
        catch (Throwable ex) {
            dB.echoError("Cannot load Depenizen support for '" + name + "': Internal exception was thrown!");
            dB.echoError(ex);
            return;
        }
        loadedBridges.put(name, newBridge);
        dB.log("Depenizen loaded support for '" + name + "'!");
    }

    public void registerCoreBridges() {
        registerBridge("AreaShop", AreaShopSupport::new);
        registerBridge("ASkyBlock", ASkyBlockSupport::new);
        registerBridge("BattleNight", BattleNightSupport::new);
        registerBridge("BossShop", BossShopSupport::new);
        registerBridge("dtlTraders", dtlTradersSupport::new);
        registerBridge("EffectLib", EffectLibSupport::new);
        registerBridge("Essentials", EssentialsSupport::new);
        registerBridge("Factions", FactionsSupport::new);
        registerBridge("GriefPrevention", GriefPreventionSupport::new);
        registerBridge("Heroes", HeroesSupport::new);
        registerBridge("HyperConomy", HyperConomySupport::new);
        registerBridge("Jobs", JobsSupport::new);
        registerBridge("LibsDisguises", LibsDisguisesSupport::new);
        registerBridge("LuckPerms", LuckPermsSupport::new);
        registerBridge("MagicSpells", MagicSpellsSupport::new);
        registerBridge("McMMO", McMMOSupport::new);
        registerBridge("MobArena", MobArenaSupport::new);
        registerBridge("MythicMobs", MythicMobsSupport::new);
        registerBridge("NoCheatPlus", NoCheatPlusSupport::new);
        registerBridge("NoteBlockAPI", NoteBlockAPISupport::new);
        registerBridge("OpenTerrainGenerator", OpenTerrainGeneratorSupport::new);
        registerBridge("PlaceholderAPI", PlaceholderAPISupport::new);
        registerBridge("PlayerPoints", PlayerPointsSupport::new);
        registerBridge("PlotMe", PlotMeSupport::new);
        registerBridge("PlotSquared", PlotSquaredSupport::new);
        registerBridge("Prism", PrismSupport::new);
        registerBridge("PVPArena", PVPArenaSupport::new);
        registerBridge("PVPStats", PVPStatsSupport::new);
        registerBridge("Quests", QuestsSupport::new);
        registerBridge("Residence", ResidenceSupport::new);
        registerBridge("Sentinel", SentinelSupport::new);
        registerBridge("Shopkeepers", ShopkeepersSupport::new);
        registerBridge("SimpleClans", SimpleClansSupport::new);
        registerBridge("SkillAPI", SkillAPISupport::new);
        registerBridge("TerrainControl", TerrainControlSupport::new);
        registerBridge("TownyChat", TownyChatSupport::new);
        registerBridge("Towny", TownySupport::new);
        registerBridge("Vampire", VampireSupport::new);
        registerBridge("Votifier", VotifierSupport::new);
        registerBridge("WorldEdit", WorldEditSupport::new);
        registerBridge("WorldGuard", WorldGuardSupport::new);
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, bridgeSupplier);
    }
}
