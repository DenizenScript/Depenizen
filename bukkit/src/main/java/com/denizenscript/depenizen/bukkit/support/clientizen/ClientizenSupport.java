package com.denizenscript.depenizen.bukkit.support.clientizen;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.commands.clientizen.ClientRunCommand;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.DataSerializer;
import net.aufdemrand.denizen.events.bukkit.ScriptReloadEvent;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.scripts.ScriptHelper;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    private static final List<UUID> playersWithMod = new ArrayList<UUID>();

    public static final File clientScriptsFolder;

    static {
        clientScriptsFolder = new File(DepenizenPlugin.getCurrentInstance().getDataFolder(), "client_scripts");
        clientScriptsFolder.mkdirs();
    }

    public ClientizenSupport() {
        new ClientRunCommand().activate().as("CLIENTRUN").withOptions("clientrun [<script_name>] (def:<name>|<value>|...)", 1);
        Bukkit.getMessenger().registerIncomingPluginChannel(DepenizenPlugin.getCurrentInstance(), "Clientizen", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(DepenizenPlugin.getCurrentInstance(), "Clientizen");
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
        reloadClientScripts();
    }

    private static void reloadClientScripts() {
        clientScripts.clear();
        List<File> files = CoreUtilities.listDScriptFiles(clientScriptsFolder);
        for (File file : files) {
            try {
                FileInputStream fis = new FileInputStream(file);
                String contents = ScriptHelper.convertStreamToString(fis);
                fis.close();
                String fileName = file.getName();
                clientScripts.put(fileName, contents);
                DepenizenPlugin.depenizenLog("Loaded client script file: " + fileName);
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playersWithMod.contains(player.getUniqueId())) {
                sendAllScripts(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (playersWithMod.contains(uuid)) {
            playersWithMod.remove(uuid);
        }
    }

    public static void sendAllScripts(Player player) {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("LoadAllScripts");
        serializer.writeStringMap(clientScripts);
        send(player, serializer);
    }

    public static void runScript(Player player, String script, Map<String, String> definitions) {
        DataSerializer serializer = new DataSerializer();
        serializer.writeString("RunScript");
        serializer.writeString(script);
        serializer.writeStringMap(definitions);
        send(player, serializer);
    }

    private static void send(Player player, DataSerializer serializer) {
        player.sendPluginMessage(DepenizenPlugin.getCurrentInstance(), "Clientizen", serializer.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("Clientizen")) {
            DataDeserializer deserializer = new DataDeserializer(bytes);
            String subchannel = deserializer.readString();
            if (subchannel.equals("READY")) {
                UUID uuid = player.getUniqueId();
                if (!playersWithMod.contains(uuid)) {
                    playersWithMod.add(uuid);
                    sendAllScripts(player);
                }
            }
        }
    }
}
