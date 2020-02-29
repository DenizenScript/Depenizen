package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ProxyPingResultPacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerListPingScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class ProxyPingPacketIn extends PacketIn {

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
        int addressLength = data.readInt();
        if (data.readableBytes() < addressLength || addressLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (address bytes requested: " + addressLength + ")");
            return;
        }
        String address = readString(data, addressLength);
        int currentPlayers = data.readInt();
        int maxPlayers = data.readInt();
        int motdLength = data.readInt();
        if (data.readableBytes() < motdLength || motdLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (motd bytes requested: " + motdLength + ")");
            return;
        }
        String motd = readString(data, motdLength);
        int protocol = data.readInt();
        int versionLength = data.readInt();
        if (data.readableBytes() < versionLength || versionLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (version bytes requested: " + versionLength + ")");
            return;
        }
        String version = readString(data, versionLength);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeProxyServerListPingScriptEvent.instance.address = address;
                        BungeeProxyServerListPingScriptEvent.instance.currentPlayers = currentPlayers;
                        BungeeProxyServerListPingScriptEvent.instance.maxPlayers = maxPlayers;
                        BungeeProxyServerListPingScriptEvent.instance.motd = motd;
                        BungeeProxyServerListPingScriptEvent.instance.protocol = protocol;
                        BungeeProxyServerListPingScriptEvent.instance.version = version;
                        BungeeProxyServerListPingScriptEvent.instance.fire();
                        ProxyPingResultPacketOut packetOut = new ProxyPingResultPacketOut();
                        packetOut.id = id;
                        packetOut.maxPlayers = BungeeProxyServerListPingScriptEvent.instance.maxPlayers;
                        packetOut.version = BungeeProxyServerListPingScriptEvent.instance.version;
                        packetOut.motd = BungeeProxyServerListPingScriptEvent.instance.motd;
                        BungeeBridge.instance.sendPacket(packetOut);
                    }
                });
    }
}
