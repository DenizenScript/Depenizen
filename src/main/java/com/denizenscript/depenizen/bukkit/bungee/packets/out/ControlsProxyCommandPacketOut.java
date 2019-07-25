package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import io.netty.buffer.ByteBuf;

public class ControlsProxyCommandPacketOut extends PacketOut {

    public ControlsProxyCommandPacketOut(boolean control) {
        this.control = control;
    }

    public boolean control;

    @Override
    public int getPacketId() {
        return 16;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeByte(control ? 1 : 0);
    }
}
