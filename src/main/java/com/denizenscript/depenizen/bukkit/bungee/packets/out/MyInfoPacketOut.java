package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class MyInfoPacketOut extends BungeePacketOut {

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
