package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ProxyCommandResultPacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerCommandScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class ProxyCommandPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "ProxyPing";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 8 + 4 + 4) {
            BungeeBridge.instance.handler.fail("Invalid ProxyCommandPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        long id = data.readLong();
        int senderLength = data.readInt();
        if (data.readableBytes() < senderLength || senderLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyCommandPacket (sender bytes requested: " + senderLength + ")");
            return;
        }
        String sender = readString(data, senderLength);
        int commandLength = data.readInt();
        if (data.readableBytes() < commandLength || commandLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid ProxyCommandPacket (command bytes requested: " + commandLength + ")");
            return;
        }
        String command = readString(data, commandLength);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeProxyServerCommandScriptEvent.instance.sender = sender;
                        BungeeProxyServerCommandScriptEvent.instance.command = command;
                        BungeeProxyServerCommandScriptEvent.instance.fire();
                        ProxyCommandResultPacketOut packetOut = new ProxyCommandResultPacketOut();
                        packetOut.id = id;
                        packetOut.result = BungeeProxyServerCommandScriptEvent.instance.cancelled ? "cancelled" :
                                (!BungeeProxyServerCommandScriptEvent.instance.command.equals(command) ?
                                        BungeeProxyServerCommandScriptEvent.instance.command : "");
                        BungeeBridge.instance.sendPacket(packetOut);
                    }
                });
    }
}
