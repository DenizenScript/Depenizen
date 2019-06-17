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
        registerBridge("AreaShop", AreaShopBridge::new);
        registerBridge("ASkyBlock", ASkyBlockBridge::new);
        registerBridge("BattleNight", BattleNightBridge::new);
        registerBridge("BossShop", BossShopBridge::new);
        registerBridge("dtlTraders", dtlTradersBridge::new);
        registerBridge("EffectLib", EffectLibBridge::new);
        registerBridge("Essentials", EssentialsBridge::new);
        registerBridge("Factions", FactionsBridge::new);
        registerBridge("GriefPrevention", GriefPreventionBridge::new);
        registerBridge("Heroes", HeroesBridge::new);
        registerBridge("HyperConomy", HyperConomyBridge::new);
        registerBridge("Jobs", JobsBridge::new);
        registerBridge("LibsDisguises", LibsDisguisesBridge::new);
        registerBridge("LuckPerms", LuckPermsBridge::new);
        registerBridge("MagicSpells", MagicSpellsBridge::new);
        registerBridge("McMMO", McMMOBridge::new);
        registerBridge("MobArena", MobArenaBridge::new);
        registerBridge("MythicMobs", MythicMobsBridge::new);
        registerBridge("NoCheatPlus", NoCheatPlusBridge::new);
        registerBridge("NoteBlockAPI", NoteBlockAPIBridge::new);
        registerBridge("OpenTerrainGenerator", OpenTerrainGeneratorBridge::new);
        registerBridge("PlaceholderAPI", PlaceholderAPIBridge::new);
        registerBridge("PlayerPoints", PlayerPointsBridge::new);
        registerBridge("PlotMe", PlotMeBridge::new);
        registerBridge("PlotSquared", PlotSquaredBridge::new);
        registerBridge("Prism", PrismBridge::new);
        registerBridge("PVPArena", PVPArenaBridge::new);
        registerBridge("PVPStats", PVPStatsBridge::new);
        registerBridge("Quests", QuestsBridge::new);
        registerBridge("Residence", ResidenceBridge::new);
        registerBridge("Sentinel", SentinelBridge::new);
        registerBridge("Shopkeepers", ShopkeepersBridge::new);
        registerBridge("SimpleClans", SimpleClansBridge::new);
        registerBridge("SkillAPI", SkillAPIBridge::new);
        registerBridge("TerrainControl", TerrainControlBridge::new);
        registerBridge("TownyChat", TownyChatBridge::new);
        registerBridge("Towny", TownyBridge::new);
        registerBridge("Vampire", VampireBridge::new);
        registerBridge("Votifier", VotifierBridge::new);
        registerBridge("WorldEdit", WorldEditBridge::new);
        registerBridge("WorldGuard", WorldGuardBridge::new);
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, bridgeSupplier);
    }
}
