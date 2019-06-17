package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import com.google.common.base.Charsets;
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
        byte[] versionBytes = version.getBytes(Charsets.UTF_8);
        buf.writeInt(versionBytes.length);
        buf.writeBytes(versionBytes);
        byte[] motdBytes = motd.getBytes(Charsets.UTF_8);
        buf.writeInt(motdBytes.length);
        buf.writeBytes(motdBytes);
    }
}
