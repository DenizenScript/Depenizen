package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerConnectScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class AddServerPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "AddServer";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 4) {
            BungeeBridge.instance.handler.fail("Invalid AddServerPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        int yourNameLength = data.readInt();
        if (data.readableBytes() < yourNameLength || yourNameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid AddServerPacket (name bytes requested: " + yourNameLength + ")");
            return;
        }
        String serverName = readString(data, yourNameLength);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeeBridge.instance.knownServers.add(serverName);
                        BungeeServerConnectScriptEvent.instance.reset();
                        BungeeServerConnectScriptEvent.instance.serverName = serverName;
                        BungeeServerConnectScriptEvent.instance.fire();
                    }
                });
    }
}
