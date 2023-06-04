package com.denizenscript.depenizen.bukkit.clientizen.network;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkManager implements PluginMessageListener {

    private static NetworkManager instance;
    private static final Map<String, ClientizenReceiver> registeredReceivers = new HashMap<>();

    private static final byte[] EMPTY = new byte[0];

    public static final int MAX_PACKET_LENGTH = Depenizen.instance.getConfig().getInt("Clientizen.max packet length", 10000);

    public static void init() {
        instance = new NetworkManager();
    }

    public static void registerInChannel(String channel, ClientizenReceiver receiver) {
        if (channel == null || receiver == null) {
            return;
        }
        if (registeredReceivers.containsKey(channel)) {
            Debug.echoError("Tried registering plugin channel '" + channel + "', but it is already registered!");
            return;
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(Depenizen.instance, channel, instance);
        registeredReceivers.put(channel, receiver);
    }

    public static void broadcast(String channel, DataSerializer message) {
        for (UUID uuid : ClientizenBridge.clientizenPlayers) {
            send(channel, Bukkit.getPlayer(uuid), message);
        }
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
        if (message.length > MAX_PACKET_LENGTH) {
            Debug.log("Packet with length " + message.length + " received from " + player.getName() + ", which exceeds the maximum packet length of " + MAX_PACKET_LENGTH + " - ignoring.");
            return;
        }
        registeredReceivers.get(channel).receive(player, new DataDeserializer(message));
    }

    @FunctionalInterface
    public interface ClientizenReceiver {
        void receive(Player player, DataDeserializer message);
    }
}
