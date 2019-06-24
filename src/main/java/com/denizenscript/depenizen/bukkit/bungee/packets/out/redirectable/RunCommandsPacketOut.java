package com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class RunCommandsPacketOut extends PacketOut {

    public RunCommandsPacketOut(String commands, String defs, boolean shouldDebug, UUID playerUUID) {
        this.commands = commands;
        this.defs = defs;
        this.shouldDebug = shouldDebug;
        this.playerUUID = playerUUID;
    }

    public String commands;

    public String defs;

    public boolean shouldDebug;

    public UUID playerUUID;

    @Override
    public int getPacketId() {
        return 58;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        writeString(buf, commands);
        writeString(buf, defs);
        buf.writeByte(shouldDebug ? 1 : 0);
        buf.writeLong(playerUUID.getMostSignificantBits());
        buf.writeLong(playerUUID.getLeastSignificantBits());
    }
}
