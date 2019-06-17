package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ProxyPingResultPacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerListPingScriptEvent;
import com.google.common.base.Charsets;
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
        byte[] addressBytes = new byte[addressLength];
        data.readBytes(addressBytes, 0, addressLength);
        String address = new String(addressBytes, Charsets.UTF_8);
        int currentPlayers = data.readInt();
        int maxPlayers = data.readInt();
        int motdLength = data.readInt();
        if (data.readableBytes() < motdLength || motdLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (motod bytes requested: " + motdLength + ")");
            return;
        }
        byte[] motdBytes = new byte[motdLength];
        data.readBytes(motdBytes, 0, motdLength);
        String motd = new String(motdBytes, Charsets.UTF_8);
        int protocol = data.readInt();
        int versionLength = data.readInt();
        if (data.readableBytes() < versionLength || versionLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyPingPacket (motod bytes requested: " + versionLength + ")");
            return;
        }
        byte[] versionBytes = new byte[versionLength];
        data.readBytes(versionBytes, 0, versionLength);
        String version = new String(versionBytes, Charsets.UTF_8);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeProxyServerListPingScriptEvent.instance.reset();
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
