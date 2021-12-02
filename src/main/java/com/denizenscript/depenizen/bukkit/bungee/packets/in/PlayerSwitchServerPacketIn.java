package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeePlayerServerSwitchScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerSwitchServerPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "PlayerSwitchServer";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 12) {
            BungeeBridge.instance.handler.fail("Invalid PlayerSwitchServerPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        long mostSigBits = data.readLong();
        long leastSigBits = data.readLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        String name = readString(data, "name");
        String serverName = readString(data, "serverName");
        if (name == null || serverName == null) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeePlayerServerSwitchScriptEvent.instance.name = name;
            BungeePlayerServerSwitchScriptEvent.instance.uuid = uuid;
            BungeePlayerServerSwitchScriptEvent.instance.newServer = serverName;
            BungeePlayerServerSwitchScriptEvent.instance.fire();
        });
    }
}
