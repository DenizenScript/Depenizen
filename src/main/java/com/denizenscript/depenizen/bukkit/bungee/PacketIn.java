package com.denizenscript.depenizen.bukkit.bungee;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

public abstract class PacketIn {

    public abstract String getName();

    public abstract void process(ByteBuf data);

    public String readString(ByteBuf buf, int length) {
        byte[] strBytes = new byte[length];
        buf.readBytes(strBytes, 0, length);
        return new String(strBytes, Charsets.UTF_8);
    }
}
