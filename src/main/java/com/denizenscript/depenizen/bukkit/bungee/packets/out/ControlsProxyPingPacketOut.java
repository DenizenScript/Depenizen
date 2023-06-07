package com.denizenscript.depenizen.bukkit.bungee.packets.out;

import com.denizenscript.depenizen.bukkit.bungee.BungeePacketOut;
import io.netty.buffer.ByteBuf;

public class ControlsProxyPingPacketOut extends BungeePacketOut {

    public ControlsProxyPingPacketOut(boolean control) {
        this.control = control;
        canBeFirstPacket = true;
    }

    public boolean control;

    @Override
    public int getPacketId() {
        return 12;
    }

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeByte(control ? 1 : 0);
    }
}
