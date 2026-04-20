package com.denizenscript.depenizen.bukkit;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.debugging.DebugSubmitter;
import com.denizenscript.depenizen.bukkit.bridges.*;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import com.denizenscript.depenizen.bukkit.utilities.BridgeLoadException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Depenizen extends JavaPlugin {

    public static Depenizen instance;

    public HashMap<String, BridgeData> allBridges = new HashMap<>();

    public HashMap<String, Bridge> loadedBridges = new HashMap<>();

    public record BridgeData(String classCheck, Supplier<Bridge> creator) {}

    @Override
    public void onEnable() {
        Debug.log("Depenizen loading...");
        saveDefaultConfig();
        instance = this;
        registerCoreBridges();
        for (Map.Entry<String, BridgeData> bridge : allBridges.entrySet()) {
            loadBridge(bridge.getKey(), bridge.getValue());
        }
        try {
            checkLoadBungeeBridge();
        }
        catch (Throwable ex) {
            Debug.echoError("Cannot load Depenizen-Bungee bridge: Internal exception was thrown!");
            Debug.echoError(ex);
        }
        checkLoadClientizenBridge();
        DebugSubmitter.debugHeaderLines.add(() -> "Depenizen Bridges loaded (" + loadedBridges.size() + "): " + ChatColor.DARK_GREEN + String.join(", ", loadedBridges.keySet()));
        Debug.log("Depenizen loaded! <A>" + loadedBridges.size() + "<W> plugin bridge(s) loaded (of <A>" + allBridges.size() + "<W> available)");
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
            return;
        }
        new BungeeBridge().init(bungeeServer, getConfig().getInt("Bungee server port", 25565));
        Debug.log("Loaded bungee bridge!");
    }

    public void checkLoadClientizenBridge() {
        if (getConfig().getBoolean("Clientizen.enabled")) {
            ClientizenBridge.init();
        }
    }

    public void loadBridge(String name, BridgeData bridgeData) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) {
            return;
        }
        if (bridgeData.classCheck() != null) {
            try {
                Class.forName(bridgeData.classCheck());
            }
            catch (ClassNotFoundException e) {
                Debug.echoError("Tried loading plugin bridge for '<Y>" + name + "<W>', but could not match class '<Y>" + bridgeData.classCheck() + "<W>'.\n" +
                        "<FORCE_ALIGN>This usually means you have a plugin with the same name as one supported by Depenizen, which should be reported to the developers of that plugin.");
                return;
            }
        }
        Bridge newBridge;
        try {
            newBridge = bridgeData.creator().get();
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
        Debug.log("Loaded bridge for '" + name + "'!");
    }

    public void registerCoreBridges() {
        // Yes it needs to be `new MyBridge()` not `MyBridge::new` - this is due to an error in the Java runtime.
        registerBridge("AreaShop", () -> new AreaShopBridge());
        registerBridge("AriKeys", () -> new AriKeysBridge());
        registerBridge("ASkyBlock", () -> new ASkyBlockBridge());
        registerBridge("AuraSkills", () -> new AuraSkillsBridge());
        registerBridge("BetonQuest", () -> new BetonQuestBridge());
        registerBridge("BigDoors", () -> new BigDoorsBridge());
        registerBridge("BossShopPro", () -> new BossShopBridge());
        registerBridge("ChestShop", () -> new ChestShopBridge());
        registerBridge("CoreProtect", () -> new CoreProtectBridge());
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
        registerBridge("MythicKeysPlugin", () -> new MythicKeysBridge());
        registerBridge("MythicMobs", () -> new MythicMobsBridge());
        registerBridge("NoCheatPlus", () -> new NoCheatPlusBridge());
        registerBridge("NoteBlockAPI", () -> new NoteBlockAPIBridge());
        registerBridge("OpenTerrainGenerator", () -> new OpenTerrainGeneratorBridge());
        registerBridge("PlaceholderAPI", () -> new PlaceholderAPIBridge());
        registerBridge("PlayerPoints", () -> new PlayerPointsBridge());
        registerBridge("PlotSquared", () -> new PlotSquaredBridge());
        registerBridge("ProjectKorra", () -> new ProjectKorraBridge());
        registerBridge("PVPArena", () -> new PVPArenaBridge());
        registerBridge("PVPStats", () -> new PVPStatsBridge());
        registerBridge("Quests", "me.pikamug.quests.Quests", () -> new QuestsBridge());
        registerBridge("Residence", () -> new ResidenceBridge());
        registerBridge("Sentinel", () -> new SentinelBridge());
        registerBridge("Shopkeepers", () -> new ShopkeepersBridge());
        registerBridge("SkillAPI", () -> new SkillAPIBridge());
        registerBridge("TerrainControl", () -> new TerrainControlBridge());
        registerBridge("Towny", () -> new TownyBridge());
        registerBridge("TownyChat", () -> new TownyChatBridge());
        registerBridge("Vampire", () -> new VampireBridge());
        registerBridge("ViaVersion", () -> new ViaVersionBridge());
        registerBridge("Vivecraft-Spigot-Extensions", () -> new ViveCraftBridge());
        registerBridge("Votifier", () -> new VotifierBridge());
        registerBridge("WorldEdit", () -> new WorldEditBridge());
        registerBridge("WorldGuard", () -> new WorldGuardBridge());
    }

    public void registerBridge(String name, Supplier<Bridge> bridgeSupplier) {
        registerBridge(name, null, bridgeSupplier);
    }

    public void registerBridge(String name, String classCheck, Supplier<Bridge> bridgeSupplier) {
        allBridges.put(name, new BridgeData(classCheck, bridgeSupplier));
    }
}
