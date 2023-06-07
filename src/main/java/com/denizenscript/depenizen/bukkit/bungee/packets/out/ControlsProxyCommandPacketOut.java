package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class ControlsProxyCommandPacketOut extends BungeePacketOut {

    public ControlsProxyCommandPacketOut(boolean control) {
        this.control = control;
        canBeFirstPacket = true;
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
