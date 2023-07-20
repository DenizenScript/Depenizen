package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerConnectScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

public class AddServerPacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "AddServer";
    }

    @Override
    public void process(ByteBuf data) {
        String serverName = readString(data, "serverName");
        if (serverName == null) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeeBridge.instance.knownServers.add(serverName);
            BungeeServerConnectScriptEvent.instance.serverName = serverName;
            BungeeServerConnectScriptEvent.instance.fire();
        });
    }
}
