package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class SendPlayerPacketOut extends PacketOut {

    public UUID playerToSend;

    public String serverTarget;

    @Override
    public int getPacketId() {
        return 10;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeLong(playerToSend.getMostSignificantBits());
        buf.writeLong(playerToSend.getLeastSignificantBits());
        byte[] serverTargetBytes = serverTarget.getBytes(Charsets.UTF_8);
        buf.writeInt(serverTargetBytes.length);
        buf.writeBytes(serverTargetBytes);
    }
}
