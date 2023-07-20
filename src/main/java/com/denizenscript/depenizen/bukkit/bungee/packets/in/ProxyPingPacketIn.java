package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ProxyPingResultPacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerListPingScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class ProxyPingPacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "ProxyPing";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 8 + 4 + 4 + 4 + 4 + 4 + 4) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        long id = data.readLong();
        String address = readString(data, "address");
        int currentPlayers = data.readInt();
        int maxPlayers = data.readInt();
        String motd = readString(data, "motd");
        int protocol = data.readInt();
        String version = readString(data, "version");
        if (address == null || motd == null || version == null) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeeProxyServerListPingScriptEvent.PingData ping = new BungeeProxyServerListPingScriptEvent.PingData();
            ping.address = address;
            ping.currentPlayers = currentPlayers;
            ping.maxPlayers = maxPlayers;
            ping.motd = motd;
            ping.protocol = protocol;
            ping.version = version;
            BungeeProxyServerListPingScriptEvent.instance.data = ping;
            BungeeProxyServerListPingScriptEvent.instance.fire();
            ProxyPingResultPacketOut packetOut = new ProxyPingResultPacketOut();
            packetOut.id = id;
            packetOut.maxPlayers = ping.maxPlayers;
            packetOut.version = ping.version;
            packetOut.motd = ping.motd;
            packetOut.playerSample = ping.playerSample;
            BungeeBridge.instance.sendPacket(packetOut);
        });
    }
}
