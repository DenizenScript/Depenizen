package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager implements PluginMessageListener {

    private static NetworkManager instance;
    private static final Map<String, InChannelRunnable> inChannelRunnables = new HashMap<>();

    private static final byte[] EMPTY = new byte[0];

    public static void init() {
        instance = new NetworkManager();
    }

    public static void registerInChannel(String channel, InChannelRunnable runnable) {
        if (channel == null || runnable == null) {
            return;
        }
        if (inChannelRunnables.containsKey(channel)) {
            Debug.echoError("Tried registering plugin channel '" + channel + "', but it is already registered!");
            return;
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(Depenizen.instance, channel, instance);
        inChannelRunnables.put(channel, runnable);
    }

    public static void send(String channel, Player target, DataSerializer message) {
        send(channel, target, message != null ? message.toByteArray() : null);
    }

    public static void send(String channel, Player target, byte[] message) {
        if (channel == null || target == null) {
            return;
        }
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(Depenizen.instance, channel)) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(Depenizen.instance, channel);
        }
        target.sendPluginMessage(Depenizen.instance, channel, message != null ? message : EMPTY);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        inChannelRunnables.get(channel).run(player, message);
    }

    @FunctionalInterface
    public interface InChannelRunnable {
        void run(Player player, byte[] message);
    }
}
