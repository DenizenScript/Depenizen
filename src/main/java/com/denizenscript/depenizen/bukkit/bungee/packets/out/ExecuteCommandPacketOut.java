package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class ExecuteCommandPacketOut extends PacketOut {

    public ExecuteCommandPacketOut(String command, UUID executingPlayer) {
        this.command = command;
        this.executingPlayer = executingPlayer;
    }

    public String command;
    public UUID executingPlayer;

    @Override
    public int getPacketId() {
        return 15;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, command);
        if (executingPlayer != null) {
            buf.writeLong(executingPlayer.getMostSignificantBits());
            buf.writeLong(executingPlayer.getLeastSignificantBits());
        }
    }
}
