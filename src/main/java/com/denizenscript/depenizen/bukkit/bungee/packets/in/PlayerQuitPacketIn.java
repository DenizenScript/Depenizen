package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeePlayerQuitsScriptEvent;
import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerQuitPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "PlayerQuit";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 12) {
            BungeeBridge.instance.handler.fail("Invalid PlayerQuitPacket (bytes available: " + data.readableBytes() + ")");
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
            BungeePlayerQuitsScriptEvent.instance.name = name;
            BungeePlayerQuitsScriptEvent.instance.uuid = uuid;
            BungeePlayerQuitsScriptEvent.instance.ip = ip;
            BungeePlayerQuitsScriptEvent.instance.fire();
        });
    }
}
