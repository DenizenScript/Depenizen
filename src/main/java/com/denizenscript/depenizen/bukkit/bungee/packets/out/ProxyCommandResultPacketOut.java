package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class ProxyCommandResultPacketOut extends PacketOut {

    public long id;

    public String result;

    @Override
    public int getPacketId() {
        return 17;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeLong(id);
        writeString(buf, result);
    }
}
