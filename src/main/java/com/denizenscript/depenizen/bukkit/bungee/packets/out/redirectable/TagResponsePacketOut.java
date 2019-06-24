package com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class TagResponsePacketOut extends PacketOut {

    public String result;

    public int id;

    @Override
    public int getPacketId() {
        return 60;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, result);
        buf.writeInt(id);
    }
}
