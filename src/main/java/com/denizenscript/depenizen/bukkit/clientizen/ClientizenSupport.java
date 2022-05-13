package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.events.bukkit.ScriptReloadEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptHelper;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ClientizenSupport implements Listener, PluginMessageListener {

    public static ClientizenSupport instance;

    public static final Map<String, String> clientizenScripts = new HashMap<>();
    public static final Set<UUID> clientizenPlayers = new HashSet<>();

    public static final File clientizenFolder;
    public static final String CHANNEL_PREFIX = "clientizen";

    static {
        clientizenFolder = new File(Denizen.getInstance().getDataFolder(), "client-scripts");
        clientizenFolder.mkdirs();
    }

    public ClientizenSupport() {
        Bukkit.getPluginManager().registerEvents(this, Depenizen.instance);
        registerInChannel(Channel.RECIVE_CONFIRM);
        registerOutChannel(Channel.REQUEST_CONFIRM);
        registerOutChannel(Channel.SET_SCRIPTS);
        registerOutChannel(Channel.ADD_SCRIPTS);
    }

    public void registerInChannel(Channel channel) {
        Bukkit.getMessenger().registerIncomingPluginChannel(Depenizen.instance, channel.getChannel(), this);
    }

    public void registerOutChannel(Channel channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(Depenizen.instance, channel.getChannel());
    }

    public static void refershClientScripts() {
        clientizenScripts.clear();
        List<File> scriptFiles = CoreUtilities.listDScriptFiles(clientizenFolder);
        for (File file : scriptFiles) {
            String name = CoreUtilities.toLowerCase(file.getName());
            if (clientizenScripts.containsKey(name)) {
                Debug.echoError("Multiple script files named '" + name + "' found in client-scripts folder!");
                continue;
            }
            try {
                FileInputStream stream = new FileInputStream(file);
                String contents = ScriptHelper.convertStreamToString(stream);
                stream.close();
                clientizenScripts.put(name, contents);
                Debug.log("Loaded client script: " + name);
            }
            catch (Exception e) {
                Debug.echoError("Failed to load client script file '" + name + "', see below stack trace:");
                Debug.echoError(e);
            }
        }
    }

    public static void resendClientScripts() {
        for (UUID uuid : clientizenPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            resendClientScriptsTo(player);
        }
    }

    public static void resendClientScriptsTo(Player player) {
        if (player == null) {
            return;
        }
        DataSerializer serializer = new DataSerializer();
        serializer.writeStringMap(clientizenScripts);
        send(player, Channel.SET_SCRIPTS, serializer);
    }

    public static void send(Player player, Channel channel, DataSerializer serializer) {
        player.sendPluginMessage(Depenizen.instance, channel.getChannel(), serializer == null ? new byte[0] : serializer.toByteArray());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(Depenizen.instance, () -> {
            send(event.getPlayer(), Channel.REQUEST_CONFIRM, null);
        }, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clientizenPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onScriptsReload(ScriptReloadEvent event) {
        refershClientScripts();
        resendClientScripts();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channelString, @NotNull Player player, @NotNull byte[] bytes) {
        Channel channel = new ElementTag(channelString.substring(CHANNEL_PREFIX.length() + 1)).asEnum(Channel.class);
        if (channel == null) {
            return;
        }
        switch (channel) {
            case RECIVE_CONFIRM:
                clientizenPlayers.add(player.getUniqueId());
        }
    }

    enum Channel {
        SET_SCRIPTS("set_scripts"),
        ADD_SCRIPTS("add_scripts"),
        REQUEST_CONFIRM("request_confirmation"),
        RECIVE_CONFIRM("receive_confirmation");

        private final String channel;

        Channel(String channel) {
            this.channel = CHANNEL_PREFIX + channel;
        }

        public String getChannel() {
            return channel;
        }

        @Override
        public String toString() {
            return channel;
        }
    }
}
