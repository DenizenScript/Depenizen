package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketIn;
import io.netty.buffer.ByteBuf;

public class KeepAlivePacketIn extends BungeePacketIn {

    @Override
    public String getName() {
        return "KeepAlive";
    }

    @Override
    public void process(ByteBuf data) {
        // Do nothing, no data is included
    }
}
