package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerDisconnectScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class RemoveServerPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "RemoveServer";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 4) {
            BungeeBridge.instance.handler.fail("Invalid RemoveServerPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        int serverNameLength = data.readInt();
        if (data.readableBytes() < serverNameLength || serverNameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid RemoveServerPacket (name bytes requested: " + serverNameLength + ")");
            return;
        }
        byte[] serverNameBytes = new byte[serverNameLength];
        data.readBytes(serverNameBytes, 0, serverNameLength);
        String serverName = readString(data, serverNameLength);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeBridge.instance.knownServers.remove(serverName);
                        BungeeServerDisconnectScriptEvent.instance.reset();
                        BungeeServerDisconnectScriptEvent.instance.serverName = serverName;
                        BungeeServerDisconnectScriptEvent.instance.fire();
                    }
                });
    }
}
