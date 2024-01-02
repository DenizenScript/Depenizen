package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeProxyServerListPingScriptEvent;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class ProxyPingResultPacketOut extends BungeePacketOut {

    public long id;

    public int maxPlayers;

    public String version;

    public String motd;

    public List<BungeeProxyServerListPingScriptEvent.PlayerInfo> playerSample;

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
        if (playerSample == null) {
            buf.writeInt(-1);
        }
        else {
            buf.writeInt(playerSample.size());
            for (int i = 0; i < playerSample.size(); i++) {
                writeString(buf, playerSample.get(i).name);
                buf.writeLong(playerSample.get(i).id.getMostSignificantBits());
                buf.writeLong(playerSample.get(i).id.getLeastSignificantBits());
            }
        }
    }
}
