package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ProxyCommandResultPacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerCommandScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ProxyCommandPacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "ProxyPing";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 8 + 4 + 4 + 4) {
            BungeeBridge.instance.handler.fail("Invalid ProxyCommandPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        long id = data.readLong();
        String sender = readString(data, "sender");
        String command = readString(data, "command");
        String senderIdText = readString(data, "senderID");
        if (sender == null || command == null || senderIdText == null) {
            return;
        }
        UUID senderId = senderIdText.isEmpty() ? null : UUID.fromString(senderIdText);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeeProxyServerCommandScriptEvent.instance.sender = sender;
            BungeeProxyServerCommandScriptEvent.instance.senderId = senderId;
            BungeeProxyServerCommandScriptEvent.instance.command = new BungeeProxyServerCommandScriptEvent.CommandData();
            BungeeProxyServerCommandScriptEvent.instance.command.command = command;
            BungeeProxyServerCommandScriptEvent.instance.fire();
            ProxyCommandResultPacketOut packetOut = new ProxyCommandResultPacketOut();
            packetOut.id = id;
            packetOut.result = BungeeProxyServerCommandScriptEvent.instance.command.cancelled ? "cancelled" :
                    (!BungeeProxyServerCommandScriptEvent.instance.command.command.equals(command) ?
                            BungeeProxyServerCommandScriptEvent.instance.command.command : "");
            BungeeBridge.instance.sendPacket(packetOut);
        });
    }
}
