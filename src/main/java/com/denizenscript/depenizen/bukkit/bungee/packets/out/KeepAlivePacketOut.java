package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class KeepAlivePacketOut extends PacketOut {

    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        // No data
    }
}
