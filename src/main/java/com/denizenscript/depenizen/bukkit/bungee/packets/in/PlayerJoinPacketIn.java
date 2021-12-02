package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeePlayerJoinsScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerJoinPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "PlayerJoin";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 12) {
            BungeeBridge.instance.handler.fail("Invalid PlayerJoinPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        long mostSigBits = data.readLong();
        long leastSigBits = data.readLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        String name = readString(data, "name");
        String ip = readString(data, "ip");
        if (name == null || ip == null) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, () -> {
            BungeePlayerJoinsScriptEvent.instance.name = name;
            BungeePlayerJoinsScriptEvent.instance.uuid = uuid;
            BungeePlayerJoinsScriptEvent.instance.ip = ip;
            BungeePlayerJoinsScriptEvent.instance.fire();
        });
    }
}
