package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class RedirectPacketOut extends BungeePacketOut {

    public RedirectPacketOut(String server, BungeePacketOut toSend) {
        this.server = server;
        this.toSend = toSend;
    }

    public String server;

    public BungeePacketOut toSend;

    @Override
    public int getPacketId() {
        return 14;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, server);
        ByteBuf nbuf = buf.alloc().buffer();
        toSend.writeTo(nbuf);
        buf.writeInt(nbuf.writerIndex());
        buf.writeInt(toSend.getPacketId());
        buf.writeBytes(nbuf);
        nbuf.release();
    }
}
