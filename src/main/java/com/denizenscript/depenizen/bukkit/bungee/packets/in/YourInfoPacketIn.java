package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import io.netty.buffer.ByteBuf;

public class YourInfoPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "YourInfo";
    }

    @Override
    public void process(ByteBuf data) {
        String serverName = readString(data, "serverName");
        if (serverName == null) {
            return;
        }
        BungeeBridge.instance.serverName = serverName;
    }
}
