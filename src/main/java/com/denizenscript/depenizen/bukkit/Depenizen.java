package com.denizenscript.depenizen.bukkit;

import com.denizenscript.depenizen.bukkit.bridges.*;
import com.denizenscript.depenizen.bukkit.utilities.BridgeLoadException;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
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
        for (Map.Entry<String, Supplier<Bridge>> bridge : allBridges.entrySet()) {
            loadBridge(bridge.getKey(), bridge.getValue());
        }
        dB.log("Depenizen loaded! " + loadedBridges.size() + " bridges loaded (of " + allBridges.size() + " available)");
    }

    public void loadBridge(String name, Supplier<Bridge> bridgeSupplier) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) {
            dB.log("<G>Depenizen will not load bridge for '" + name + "'.");
            return;
        }
        Bridge newBridge;
        try {
            newBridge = bridgeSupplier.get();
        }
        catch (Throwable ex) {
            dB.echoError("Cannot load Depenizen bridge for '" + name + "': fundamental loading error: " + ex.getMessage());
            return;
        }
        newBridge.name = name;
        newBridge.plugin = plugin;
        try {
            newBridge.init();
        }
        catch (BridgeLoadException ex) {
            dB.echoError("Cannot load Depenizen bridge for '" + name + "': " + ex.getMessage());
            return;
        }
        catch (Throwable ex) {
            dB.echoError("Cannot load Depenizen bridge for '" + name + "': Internal exception was thrown!");
            dB.echoError(ex);
            return;
        }
        loadedBridges.put(name, newBridge);
        dB.log("Depenizen loaded bridge for '" + name + "'!");
    }

    public void registerCoreBridges() {
        // Yes it needs to be `new MyBridge()` not `MyBridge::new` - this is due to an error in the Java runtime.
        registerBridge("AreaShop", () -> new AreaShopBridge());
        registerBridge("ASkyBlock", () -> new ASkyBlockBridge());
        registerBridge("BattleNight", () -> new BattleNightBridge());
        registerBridge("BossShop", () -> new BossShopBridge());
        registerBridge("dtlTraders", () -> new dtlTradersBridge());
        registerBridge("EffectLib", () -> new EffectLibBridge());
        registerBridge("Essentials", () -> new EssentialsBridge());
        registerBridge("Factions", () -> new FactionsBridge());
        registerBridge("GriefPrevention", () -> new GriefPreventionBridge());
        registerBridge("Heroes", () -> new HeroesBridge());
        registerBridge("HyperConomy", () -> new HyperConomyBridge());
        registerBridge("Jobs", () -> new JobsBridge());
        registerBridge("LibsDisguises", () -> new LibsDisguisesBridge());
        registerBridge("LuckPerms", () -> new LuckPermsBridge());
        registerBridge("MagicSpells", () -> new MagicSpellsBridge());
        registerBridge("McMMO", () -> new McMMOBridge());
        registerBridge("MobArena", () -> new MobArenaBridge());
        registerBridge("MythicMobs", () -> new MythicMobsBridge());
        registerBridge("NoCheatPlus", () -> new NoCheatPlusBridge());
        registerBridge("NoteBlockAPI", () -> new NoteBlockAPIBridge());
        registerBridge("OpenTerrainGenerator", () -> new OpenTerrainGeneratorBridge());
        registerBridge("PlaceholderAPI", () -> new PlaceholderAPIBridge());
        registerBridge("PlayerPoints", () -> new PlayerPointsBridge());
        registerBridge("PlotMe", () -> new PlotMeBridge());
        registerBridge("PlotSquared", () -> new PlotSquaredBridge());
        registerBridge("Prism", () -> new PrismBridge());
        registerBridge("PVPArena", () -> new PVPArenaBridge());
        registerBridge("PVPStats", () -> new PVPStatsBridge());
        registerBridge("Quests", () -> new QuestsBridge());
        registerBridge("Residence", () -> new ResidenceBridge());
        registerBridge("Sentinel", () -> new SentinelBridge());
        registerBridge("Shopkeepers", () -> new ShopkeepersBridge());
        registerBridge("SimpleClans", () -> new SimpleClansBridge());
        registerBridge("SkillAPI", () -> new SkillAPIBridge());
        registerBridge("TerrainControl", () -> new TerrainControlBridge());
        registerBridge("TownyChat", () -> new TownyChatBridge());
        registerBridge("Towny", () -> new TownyBridge());
        registerBridge("Vampire", () -> new VampireBridge());
        registerBridge("Votifier", () -> new VotifierBridge());
        registerBridge("WorldEdit", () -> new WorldEditBridge());
        registerBridge("WorldGuard", () -> new WorldGuardBridge());
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, bridgeSupplier);
    }
}
