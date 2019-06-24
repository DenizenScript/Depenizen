package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class ExecuteCommandPacketOut extends PacketOut {

    public ExecuteCommandPacketOut(String command) {
        this.command = command;
    }

    public String command;

    @Override
    public int getPacketId() {
        return 15;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, command);
    }
}
