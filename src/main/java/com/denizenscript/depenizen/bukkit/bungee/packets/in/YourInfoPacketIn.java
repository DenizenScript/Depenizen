package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

public class YourInfoPacketIn extends PacketIn {

    @Override
    public String getName() {
        return "YourInfo";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 4) {
            BungeeBridge.instance.handler.fail("Invalid YourInfoPacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        int yourNameLength = data.readInt();
        if (data.readableBytes() < yourNameLength || yourNameLength < 0) {
            BungeeBridge.instance.handler.fail("Invalid YourInfoPacket (name bytes requested: " + yourNameLength + ")");
            return;
        }
        String serverName = readString(data, yourNameLength);
        BungeeBridge.instance.serverName = serverName;
    }
}
