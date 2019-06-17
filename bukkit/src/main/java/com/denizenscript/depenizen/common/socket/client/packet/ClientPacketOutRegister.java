package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutRegister extends Packet {

    private String name;
    private boolean bungeeScriptCompatible;

    public ClientPacketOutRegister(String name, boolean bungeeScriptCompatible) {
        this.name = name;
        this.bungeeScriptCompatible = bungeeScriptCompatible;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.REGISTER.getId());
        serializer.writeString(name);
        serializer.writeBoolean(bungeeScriptCompatible);
    }
}
