package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class MyInfoPacketOut extends PacketOut {

    public MyInfoPacketOut(int port) {
        this.port = port;
    }

    public int port;

    @Override
    public int getPacketId() {
        return 11;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeInt(port);
    }
}
