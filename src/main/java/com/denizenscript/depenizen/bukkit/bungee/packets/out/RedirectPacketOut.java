package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class RedirectPacketOut extends PacketOut {

    public RedirectPacketOut(String server, PacketOut toSend) {
        this.server = server;
        this.toSend = toSend;
    }

    public String server;

    public PacketOut toSend;

    @Override
    public int getPacketId() {
        return 14;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, server);
        ByteBuf nbuf = buf.alloc().buffer();
        toSend.writeTo(nbuf);
        ByteBuf header = buf.alloc().buffer();
        header.writeInt(nbuf.writerIndex());
        header.writeInt(toSend.getPacketId());
        buf.writeBytes(header);
        buf.writeBytes(nbuf);
        header.release();
        nbuf.release();
    }
}
