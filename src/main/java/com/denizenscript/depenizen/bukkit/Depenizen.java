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
        registerBridge("AreaShop", () -> new AreaShopBridge());
        registerBridge("ASkyBlock", () -> new ASkyBlockBridge());
        registerBridge("BigDoors", () -> new BigDoorsBridge());
        registerBridge("BossShopPro", () -> new BossShopBridge());
        registerBridge("CrackShot", () -> new CrackShotBridge());
        registerBridge("EffectLib", () -> new EffectLibBridge());
        registerBridge("Essentials", () -> new EssentialsBridge());
        registerBridge("Factions", () -> new FactionsBridge());
        registerBridge("GriefPrevention", () -> new GriefPreventionBridge());
        registerBridge("Jobs", () -> new JobsBridge());
        registerBridge("LibsDisguises", () -> new LibsDisguisesBridge());
        registerBridge("LuckPerms", () -> new LuckPermsBridge());
        registerBridge("MagicSpells", () -> new MagicSpellsBridge());
        registerBridge("mcMMO", () -> new McMMOBridge());
        registerBridge("MobArena", () -> new MobArenaBridge());
        registerBridge("MythicMobs", () -> new MythicMobsBridge());
        registerBridge("NoCheatPlus", () -> new NoCheatPlusBridge());
        registerBridge("NoteBlockAPI", () -> new NoteBlockAPIBridge());
        registerBridge("OpenTerrainGenerator", () -> new OpenTerrainGeneratorBridge());
        registerBridge("PlaceholderAPI", () -> new PlaceholderAPIBridge());
        registerBridge("PlayerPoints", () -> new PlayerPointsBridge());
        registerBridge("PlotSquared", () -> new PlotSquaredBridge());
        registerBridge("PVPArena", () -> new PVPArenaBridge());
        registerBridge("PVPStats", () -> new PVPStatsBridge());
        registerBridge("Quests", () -> new QuestsBridge());
        registerBridge("Residence", () -> new ResidenceBridge());
        registerBridge("Sentinel", () -> new SentinelBridge());
        registerBridge("Shopkeepers", () -> new ShopkeepersBridge());
        registerBridge("SkillAPI", () -> new SkillAPIBridge());
        registerBridge("TerrainControl", () -> new TerrainControlBridge());
        registerBridge("TownyChat", () -> new TownyChatBridge());
        registerBridge("Towny", () -> new TownyBridge());
        registerBridge("Vampire", () -> new VampireBridge());
        registerBridge("ViaVersion", () -> new ViaVersionBridge());
        registerBridge("Votifier", () -> new VotifierBridge());
        registerBridge("WorldEdit", () -> new WorldEditBridge());
        registerBridge("WorldGuard", () -> new WorldGuardBridge());
        registerBridge("BetonQuest", () -> new BetonQuestBridge());
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, bridgeSupplier);
    }
}
