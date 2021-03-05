package com.denizenscript.depenizen.bukkit;
import com.denizenscript.denizen.Denizen;
import com.denizenscript.depenizen.bukkit.bridges.*;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.utilities.BridgeLoadException;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
        Debug.log("Depenizen loading...");
        saveDefaultConfig();
        instance = this;
        registerCoreBridges();
        for (Map.Entry<String, Supplier<Bridge>> bridge : allBridges.entrySet()) {
            loadBridge(bridge.getKey(), bridge.getValue());
        }
        try {
            checkLoadBungeeBridge();
        }
        catch (Throwable ex) {
            Debug.echoError("Cannot load Depenizen-Bungee bridge: Internal exception was thrown!");
            Debug.echoError(ex);
        }
        Debug.log("Depenizen loaded! " + loadedBridges.size() + " plugin bridge(s) loaded (of " + allBridges.size() + " available)");
    }

    @Override
    public void onDisable() {
        if (BungeeBridge.instance != null && BungeeBridge.instance.connected) {
            BungeeBridge.instance.onShutdown();
        }
        // To prevent issue with onDisable order, tell Denizen to shutdown NOW (it won't repeat itself).
        Denizen.getInstance().onDisable();
    }

    public void checkLoadBungeeBridge() {
        String bungeeServer = getConfig().getString("Bungee server address", "none");
        if (CoreUtilities.equalsIgnoreCase(bungeeServer, "none")) {
            Debug.log("<G>Depenizen will not load bungee bridge.");
            return;
        }
        new BungeeBridge().init(bungeeServer, getConfig().getInt("Bungee server port", 25565));
        Debug.log("Depenizen loaded bungee bridge!");
    }

    public void loadBridge(String name, Supplier<Bridge> bridgeSupplier) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) {
            Debug.log("<G>Depenizen will not load bridge for '" + name + "'.");
            return;
        }
        Bridge newBridge;
        try {
            newBridge = bridgeSupplier.get();
        }
        catch (Throwable ex) {
            Debug.echoError("Cannot load Depenizen bridge for '" + name + "': fundamental loading error:");
            Debug.echoError(ex);
            return;
        }
        newBridge.name = name;
        newBridge.plugin = plugin;
        try {
            newBridge.init();
        }
        catch (BridgeLoadException ex) {
            Debug.echoError("Cannot load Depenizen bridge for '" + name + "': " + ex.getMessage());
            return;
        }
        catch (Throwable ex) {
            Debug.echoError("Cannot load Depenizen bridge for '" + name + "': Internal exception was thrown!");
            Debug.echoError(ex);
            return;
        }
        loadedBridges.put(name, newBridge);
        Debug.log("Depenizen loaded bridge for '" + name + "'!");
    }

    public void registerCoreBridges() {
        // Yes it needs to be `new MyBridge()` not `MyBridge::new` - this is due to an error in the Java runtime.
        registerBridge("AreaShop", AreaShopBridge::new);
        registerBridge("ASkyBlock", ASkyBlockBridge::new);
        registerBridge("BigDoors", BigDoorsBridge::new);
        registerBridge("BossShopPro", BossShopBridge::new);
        registerBridge("CrackShot", CrackShotBridge::new);
        registerBridge("EffectLib", EffectLibBridge::new);
        registerBridge("Essentials", EssentialsBridge::new);
        registerBridge("Factions", FactionsBridge::new);
        registerBridge("GriefPrevention", GriefPreventionBridge::new);
        registerBridge("Jobs", JobsBridge::new);
        registerBridge("LibsDisguises", LibsDisguisesBridge::new);
        registerBridge("LuckPerms", LuckPermsBridge::new);
        registerBridge("MagicSpells", MagicSpellsBridge::new);
        registerBridge("mcMMO", McMMOBridge::new);
        registerBridge("MobArena", MobArenaBridge::new);
        registerBridge("MythicMobs", MythicMobsBridge::new);
        registerBridge("NoCheatPlus", NoCheatPlusBridge::new);
        registerBridge("NoteBlockAPI", NoteBlockAPIBridge::new);
        registerBridge("OpenTerrainGenerator", OpenTerrainGeneratorBridge::new);
        registerBridge("PlaceholderAPI", PlaceholderAPIBridge::new);
        registerBridge("PlayerPoints", PlayerPointsBridge::new);
        registerBridge("PlotSquared", PlotSquaredBridge::new);
        registerBridge("PVPArena", PVPArenaBridge::new);
        registerBridge("PVPStats", PVPStatsBridge::new);
        registerBridge("Quests", QuestsBridge::new);
        registerBridge("Residence", ResidenceBridge::new);
        registerBridge("Sentinel", SentinelBridge::new);
        registerBridge("Shopkeepers", ShopkeepersBridge::new);
        registerBridge("SkillAPI", SkillAPIBridge::new);
        registerBridge("TerrainControl", TerrainControlBridge::new);
        registerBridge("TownyChat", TownyChatBridge::new);
        registerBridge("Towny", TownyBridge::new);
        registerBridge("Vampire", VampireBridge::new);
        registerBridge("ViaVersion", ViaVersionBridge::new);
        registerBridge("Votifier", VotifierBridge::new);
        registerBridge("WorldEdit", WorldEditBridge::new);
        registerBridge("WorldGuard", WorldGuardBridge::new);
        registerBridge("BetonQuest", BetonQuestBridge::new);
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, bridgeSupplier);
    }
}
