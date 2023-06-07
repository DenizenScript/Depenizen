package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerDisconnectScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class RemoveServerPacketIn extends BungeePacketIn {

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
        String serverName = readString(data, "serverName");
        if (serverName == null) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeeBridge.instance.knownServers.remove(serverName);
            BungeeServerDisconnectScriptEvent.instance.serverName = serverName;
            BungeeServerDisconnectScriptEvent.instance.fire();
        });
    }
}
