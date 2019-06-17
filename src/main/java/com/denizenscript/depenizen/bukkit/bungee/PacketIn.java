package com.denizenscript.depenizen.bukkit.bungee;

import io.netty.buffer.ByteBuf;

public abstract class PacketIn {

    public abstract String getName();

    public abstract void process(ByteBuf data);
}
