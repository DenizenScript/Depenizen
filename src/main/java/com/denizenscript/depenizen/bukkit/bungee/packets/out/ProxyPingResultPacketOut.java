package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class ProxyPingResultPacketOut extends PacketOut {

    public long id;

    public int maxPlayers;

    public String version;

    public String motd;

    @Override
    public int getPacketId() {
        return 13;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeLong(id);
        buf.writeInt(maxPlayers);
        writeString(buf, version);
        writeString(buf, motd);
    }
}
