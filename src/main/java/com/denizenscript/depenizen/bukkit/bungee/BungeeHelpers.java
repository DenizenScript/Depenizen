package com.denizenscript.depenizen.bukkit.bungee;

import com.denizenscript.depenizen.bukkit.bungee.packets.out.SendPlayerPacket;

import java.util.UUID;

public class BungeeHelpers {

    public static void sendPlayer(UUID id, String server) {
        SendPlayerPacket packet = new SendPlayerPacket();
        packet.playerToSend = id;
        packet.serverTarget = server;
        BungeeBridge.instance.sendPacket(packet);
    }
}
