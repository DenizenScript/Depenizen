package com.denizenscript.depenizen.bukkit.clientizen.network;

import com.denizenscript.depenizen.bukkit.networking.PacketIn;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

public abstract class ClientizenPacketIn extends PacketIn {

    @Override
    public void process(ByteBuf data) {
        throw new UnsupportedOperationException();
    }

    public abstract void process(Player sender, ByteBuf data);
}
