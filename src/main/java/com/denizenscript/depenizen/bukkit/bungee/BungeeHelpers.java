package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.bungee.packets.out.SendPlayerPacketOut;

import java.util.UUID;

public class BungeeHelpers {

    public static void sendPlayer(UUID id, String server) {
        SendPlayerPacketOut packet = new SendPlayerPacketOut();
        packet.playerToSend = id;
        packet.serverTarget = server;
        BungeeBridge.instance.sendPacket(packet);
    }
}
