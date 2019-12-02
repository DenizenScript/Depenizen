package com.denizenscript.depenizen.bukkit.bungee;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

public abstract class PacketOut {

    public boolean canBeFirstPacket = false;

    public abstract int getPacketId();

    public abstract void writeTo(ByteBuf buf);

    public void writeString(ByteBuf buf, String str) {
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
}
