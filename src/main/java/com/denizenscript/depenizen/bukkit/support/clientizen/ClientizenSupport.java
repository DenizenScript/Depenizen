package com.denizenscript.depenizen.bukkit.support.clientizen;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.commands.clientizen.ClientRunCommand;
import com.denizenscript.depenizen.bukkit.commands.clientizen.ClientScriptsCommand;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import net.aufdemrand.denizen.events.bukkit.ScriptReloadEvent;
import net.aufdemrand.denizen.nms.NMSHandler;
import net.aufdemrand.denizen.nms.NMSVersion;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.scripts.ScriptHelper;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientizenSupport extends Support implements Listener, PluginMessageListener {

    private static final Map<String, String> clientScripts = new HashMap<String, String>();
    private static final List<String> autoScripts = new ArrayList<String>();
    private static final List<UUID> playersWithMod = new ArrayList<UUID>();
    private static final Map<UUID, List<String>> playerScripts = new HashMap<UUID, List<String>>();

    public static final File clientScriptsFolder;
    public static final String clientScriptsPath;
    public static final String autoScriptsPath;

    static {
        clientScriptsFolder = new File(DepenizenPlugin.getCurrentInstance().getDataFolder(), "client_scripts");
        clientScriptsFolder.mkdirs();
        File autoScriptsFolder = new File(clientScriptsFolder, "auto");
        autoScriptsFolder.mkdirs();
        String scriptsPath = null;
        String autoPath = null;
        try {
            scriptsPath = clientScriptsFolder.getCanonicalPath();
            autoPath = autoScriptsFolder.getCanonicalPath();
        }
        catch (IOException e) {
            DepenizenPlugin.getCurrentInstance().debugException(e);
        }
        clientScriptsPath = scriptsPath;
        autoScriptsPath = autoPath;
    }

    public static String getChannelId() {
        return NMSHandler.getVersion().isAtLeast(NMSVersion.v1_13_R2) ? "depenizen:clientizen" : "Clientizen";
    }

    private String channelId;

    public ClientizenSupport() {
        new ClientRunCommand().activate().as("CLIENTRUN").withOptions("clientrun [<script_name>] (def:<name>|<value>|...)", 1);
        new ClientScriptsCommand().activate().as("CLIENTSCRIPTS").withOptions("clientscripts [add/remove] [<file_name>|...] (players:<player>|...)", 2);
        channelId = getChannelId();
        Bukkit.getMessenger().registerIncomingPluginChannel(DepenizenPlugin.getCurrentInstance(), channelId, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(DepenizenPlugin.getCurrentInstance(), channelId);
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
        reloadClientScripts();
    }

    private static void reloadClientScripts() {
        clientScripts.clear();
        List<File> files = CoreUtilities.listDScriptFiles(clientScriptsFolder);
        for (File file : files) {
            try {
                String path = file.getCanonicalPath();
                String fileName = CoreUtilities.toLowerCase(file.getName());
                if (clientScripts.containsKey(fileName)) {
                    DepenizenPlugin.depenizenLog("Multiple files named '" + fileName + "' found! " +
                            "Please remember Denizen is case-insensitive and client scripts do " +
                            "not support similarly named files, even across subfolders.");
                    continue;
                }
                FileInputStream fis = new FileInputStream(file);
                String contents = ScriptHelper.convertStreamToString(fis);
                fis.close();
                clientScripts.put(fileName, contents);
                if (path.startsWith(autoScriptsPath)) {
                    autoScripts.add(fileName);
                    DepenizenPlugin.depenizenLog("Loaded auto-send client script file: " + fileName);
                }
                else {
                    DepenizenPlugin.depenizenLog("Loaded dynamic client script file: " + fileName);
                }
            }
            catch (FileNotFoundException e) {
                DepenizenPlugin.getCurrentInstance().debugException(e);
            }
            catch (IOException e) {
                DepenizenPlugin.getCurrentInstance().debugException(e);
            }
        }
    }

    @EventHandler
    public void onScriptReload(ScriptReloadEvent event) {
        reloadClientScripts();
        int count = 0;
        int ignored = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playersWithMod.contains(player.getUniqueId())) {
                resendAllScripts(player);
                count++;
            }
            else {
                ignored++;
            }
        }
        DepenizenPlugin.depenizenLog("Sent client scripts to " + count + " and not to " + ignored + "!");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playersWithMod.contains(uuid)) {
            playersWithMod.remove(uuid);
            playerScripts.remove(uuid);
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(DepenizenPlugin.getCurrentInstance(), new Runnable() {
            @Override
            public void run() {
                if (event.getPlayer().isOnline()) {
                    DataSerializer serializer = new DataSerializer();
                    serializer.writeString("RequestConfirmation");
                    send(event.getPlayer(), serializer);
                }
            }
        }, 10);
    }

    public static boolean isScriptLoaded(String scriptName) {
        return clientScripts.containsKey(CoreUtilities.toLowerCase(scriptName));
    }

    public static void resendAllScripts(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playersWithMod.contains(uuid)) {
            return;
        }
        List<String> currentScripts = playerScripts.get(uuid);
        Map<String, String> scripts = new HashMap<String, String>();
        for (String scriptName : currentScripts) {
            if (clientScripts.containsKey(scriptName)) {
                scripts.put(scriptName, clientScripts.get(scriptName));
            }
        }
        for (String autoScript : autoScripts) {
            if (!scripts.containsKey(autoScript)) {
                scripts.put(autoScript, clientScripts.get(autoScript));
            }
        }
        currentScripts.clear();
        for (String newScript : scripts.keySet()) {
            currentScripts.add(newScript);
        }
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("ClearAndLoadScripts");
        serializer.writeStringMap(scripts);
        send(player, serializer);
    }

    public static void sendAutoScripts(Player player) {
        sendScripts(player, autoScripts);
    }

    public static void sendScripts(Player player, List<String> scriptNames) {
        UUID uuid = player.getUniqueId();
        if (!playersWithMod.contains(uuid)) {
            return;
        }
        List<String> currentScripts = playerScripts.get(uuid);
        Map<String, String> scripts = new HashMap<String, String>();
        for (String scriptName : scriptNames) {
            scriptName = CoreUtilities.toLowerCase(scriptName);
            if (clientScripts.containsKey(scriptName) && !currentScripts.contains(scriptName)) {
                scripts.put(scriptName, clientScripts.get(scriptName));
                currentScripts.add(scriptName);
            }
        }
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("LoadScripts");
        serializer.writeStringMap(scripts);
        send(player, serializer);
    }

    public static void removeScripts(Player player, List<String> scriptNames) {
        UUID uuid = player.getUniqueId();
        if (!playersWithMod.contains(uuid)) {
            return;
        }
        List<String> currentScripts = playerScripts.get(uuid);
        for (String scriptName : scriptNames) {
            scriptName = CoreUtilities.toLowerCase(scriptName);
            if (currentScripts.contains(scriptName)) {
                currentScripts.remove(scriptName);
            }
        }
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("RemoveScripts");
        serializer.writeStringList(scriptNames);
        send(player, serializer);
    }

    public static void runScript(Player player, String script, Map<String, String> definitions) {
        UUID uuid = player.getUniqueId();
        if (!playersWithMod.contains(uuid)) {
            return;
        }
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("RunScript");
        serializer.writeString(script);
        serializer.writeStringMap(definitions);
        send(player, serializer);
    }

    private static void send(Player player, DataSerializer serializer) {
        player.sendPluginMessage(DepenizenPlugin.getCurrentInstance(), getChannelId(), serializer.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals(channelId)) {
            DataDeserializer deserializer = new DataDeserializer(bytes);
            String subchannel = deserializer.readString();
            if (subchannel.equals("READY")) {
                UUID uuid = player.getUniqueId();
                if (!playersWithMod.contains(uuid)) {
                    playersWithMod.add(uuid);
                    playerScripts.put(uuid, new ArrayList<String>());
                    sendAutoScripts(player);
                }
            }
        }
    }
}
