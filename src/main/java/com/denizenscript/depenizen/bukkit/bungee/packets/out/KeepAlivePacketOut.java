package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class KeepAlivePacketOut extends PacketOut {

    @Override
    public int getPacketId() {
        return 0;
    }

    public static byte[] KEEP_ALIVE_BUFFER = new byte[512];

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeBytes(KEEP_ALIVE_BUFFER);
    }
}
