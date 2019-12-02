package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class MyInfoPacketOut extends PacketOut {

    public MyInfoPacketOut(int port) {
        this.port = port;
        canBeFirstPacket = true;
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
