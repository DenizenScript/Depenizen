package com.denizenscript.depenizen.bukkit.bungee;

import io.netty.buffer.ByteBuf;

public abstract class PacketOut {

    public abstract int getPacketId();

    public abstract void writeTo(ByteBuf buf);
}
