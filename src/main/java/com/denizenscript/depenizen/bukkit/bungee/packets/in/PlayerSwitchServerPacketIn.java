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
        int nameLength = data.readInt();
        if (data.readableBytes() < nameLength || nameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid PlayerSwitchServerPacket (name bytes requested: " + nameLength + ")");
            return;
        }
        String name = readString(data, nameLength);
        int serverNameLength = data.readInt();
        if (data.readableBytes() < serverNameLength || serverNameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid PlayerSwitchServerPacket (name bytes requested: " + serverNameLength + ")");
            return;
        }
        String serverName = readString(data, serverNameLength);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeePlayerServerSwitchScriptEvent.instance.name = name;
            BungeePlayerServerSwitchScriptEvent.instance.uuid = uuid;
            BungeePlayerServerSwitchScriptEvent.instance.newServer = serverName;
            BungeePlayerServerSwitchScriptEvent.instance.fire();
        });
    }
}
