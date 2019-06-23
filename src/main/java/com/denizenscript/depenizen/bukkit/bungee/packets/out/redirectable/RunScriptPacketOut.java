package com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class RunScriptPacketOut extends PacketOut {

    public String scriptName;

    public UUID playerUUID;

    public String defs;

    @Override
    public int getPacketId() {
        return 57;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, scriptName);
        writeString(buf, defs);
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
    }
}
