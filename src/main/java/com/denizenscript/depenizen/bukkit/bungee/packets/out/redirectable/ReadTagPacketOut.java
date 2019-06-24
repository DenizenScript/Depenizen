package com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class ReadTagPacketOut extends PacketOut {

    public String tag;

    public UUID playerUUID;

    public String defs;

    public int id;

    @Override
    public int getPacketId() {
        return 59;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, BungeeBridge.instance.serverName);
        writeString(buf, tag);
        writeString(buf, defs);
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
        buf.writeInt(id);
    }
}
