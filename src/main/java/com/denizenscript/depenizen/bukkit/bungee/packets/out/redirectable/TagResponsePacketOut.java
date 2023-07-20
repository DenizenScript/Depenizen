package com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class TagResponsePacketOut extends BungeePacketOut {

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
