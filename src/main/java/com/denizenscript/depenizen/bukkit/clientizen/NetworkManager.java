package com.denizenscript.depenizen.bukkit.clientizen;

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

    public static void init() {
        instance = new NetworkManager();
    }

    public static void registerInChannel(String channel, InChannelRunnable runnable) {
        if (channel == null || runnable == null) {
            return;
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(Depenizen.instance, channel, instance);
        inChannelRunnables.put(channel, runnable);
    }

    public static void send(String channel, Player target, DataSerializer message) {
        if (channel == null || target == null || message == null) {
            return;
        }
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(Depenizen.instance, channel)) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(Depenizen.instance, channel);
        }
        target.sendPluginMessage(Depenizen.instance, channel, message.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        inChannelRunnables.get(channel).run(player, message);
    }

    @FunctionalInterface
    public interface InChannelRunnable {
        public void run(Player player, byte[] message);
    }
}
