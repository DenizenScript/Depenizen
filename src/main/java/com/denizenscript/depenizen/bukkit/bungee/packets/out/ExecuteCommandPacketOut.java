package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class ExecuteCommandPacketOut extends BungeePacketOut {

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
