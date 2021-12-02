package com.denizenscript.depenizen.bukkit.bungee.packets.in;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketIn;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeTagCommand;
import io.netty.buffer.ByteBuf;

public class TagResponsePacketIn extends PacketIn {

    @Override
    public String getName() {
        return "TagResponse";
    }

    @Override
    public void process(ByteBuf data) {
        if (data.readableBytes() < 4 + 4) {
            BungeeBridge.instance.handler.fail("Invalid TagResponsePacket (bytes available: " + data.readableBytes() + ")");
            return;
        }
        String result = readString(data, "result");
        if (result == null) {
            return;
        }
        int id = data.readInt();
        BungeeTagCommand.handleResult(id, result);
    }
}
