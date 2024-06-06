package com.denizenscript.depenizen.bukkit.clientizen.network.packets;

import com.denizenscript.depenizen.bukkit.clientizen.network.ClientizenPacketOut;
import com.denizenscript.depenizen.bukkit.clientizen.network.NetworkManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;

public class SetScriptsPacketOut extends ClientizenPacketOut {

    public SetScriptsPacketOut(Map<String, String> scripts) {
        ByteBuf buf = Unpooled.buffer();
        writeStringMap(buf, scripts);
        this.scriptsData = NetworkManager.bufToBytes(buf);
    }

    private final byte[] scriptsData;

    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeBytes(scriptsData);
    }

    @Override
    public String getName() {
        return "set_scripts";
    }
}
