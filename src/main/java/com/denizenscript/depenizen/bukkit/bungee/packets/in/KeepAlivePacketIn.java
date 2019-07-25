package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import io.netty.buffer.ByteBuf;

public class KeepAlivePacketIn extends PacketIn {

    @Override
    public String getName() {
        return "KeepAlive";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 1024) {
            BungeeBridge.instance.handler.fail("Invalid KeepAlivePacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        // Read and ignore empty buffer
        data.readBytes(1024);
    }
}
