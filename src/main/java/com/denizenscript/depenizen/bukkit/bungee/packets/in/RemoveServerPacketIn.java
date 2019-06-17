package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerDisconnectScriptEvent;
import com.google.common.base.Charsets;
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
        int yourNameLength = data.readInt();
        if (data.readableBytes() < yourNameLength || yourNameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid RemoveServerPacket (name bytes requested: " + yourNameLength + ")");
            return;
        }
        byte[] serverNameBytes = new byte[yourNameLength];
        data.readBytes(serverNameBytes, 0, yourNameLength);
        String serverName = new String(serverNameBytes, Charsets.UTF_8);
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
