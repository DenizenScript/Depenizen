package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeePlayerJoinsScriptEvent;
import com.google.common.base.Charsets;
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
        int nameLength = data.readInt();
        if (data.readableBytes() < nameLength || nameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid PlayerJoinPacket (name bytes requested: " + nameLength + ")");
            return;
        }
        byte[] nameBytes = new byte[nameLength];
        data.readBytes(nameBytes, 0, nameLength);
        String name = new String(nameBytes, Charsets.UTF_8);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Depenizen.instance, new Runnable() {
                    @Override
                    public void run() {
                        BungeePlayerJoinsScriptEvent.instance.reset();
                        BungeePlayerJoinsScriptEvent.instance.name = name;
                        BungeePlayerJoinsScriptEvent.instance.uuid = uuid;
                        BungeePlayerJoinsScriptEvent.instance.fire();
                    }
                });
    }
}
