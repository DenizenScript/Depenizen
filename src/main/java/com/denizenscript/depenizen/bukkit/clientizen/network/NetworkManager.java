package com.denizenscript.depenizen.bukkit.clientizen.network;

import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkManager implements PluginMessageListener {

    private static NetworkManager instance;
    private static final Map<String, ClientizenPacketIn> IN_PACKETS = new HashMap<>();
    public static final String CHANNEL_NAMESPACE = "clientizen";

    public static final int MAX_PACKET_LENGTH = Depenizen.instance.getConfig().getInt("Clientizen.max packet length", 10000);

    public static void init() {
        instance = new NetworkManager();
    }

    public static String channel(String path) {
        return CHANNEL_NAMESPACE + ':' + path;
    }

    public static void registerInPacket(ClientizenPacketIn packet) {
        String channel = channel(packet.getName());
        if (IN_PACKETS.containsKey(channel)) {
            Debug.echoError("Tried registering in packet on channel '" + channel + "', but it is already registered!");
            return;
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(Depenizen.instance, channel, instance);
        IN_PACKETS.put(channel, packet);
    }

    public static void broadcast(ClientizenPacketOut packet) {
        for (UUID uuid : ClientizenBridge.clientizenPlayers) {
            send(Bukkit.getPlayer(uuid), packet);
        }
    }

    public static void send(Player target, ClientizenPacketOut packet) {
        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(Depenizen.instance, packet.channel)) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(Depenizen.instance, packet.channel);
        }
        ByteBuf buf = Unpooled.buffer();
        packet.writeTo(buf);
        target.sendPluginMessage(Depenizen.instance, packet.channel, bufToBytes(buf));
    }

    public static byte[] bufToBytes(ByteBuf buf) {
        return ByteBufUtil.getBytes(buf, buf.readerIndex(), buf.readableBytes(), false);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (message.length > MAX_PACKET_LENGTH) {
            Debug.log("Packet with length " + message.length + " received from " + player.getName() + ", which exceeds the maximum packet length of " + MAX_PACKET_LENGTH + " - ignoring.");
            return;
        }
        IN_PACKETS.get(channel).process(player, Unpooled.wrappedBuffer(message));
    }
}
