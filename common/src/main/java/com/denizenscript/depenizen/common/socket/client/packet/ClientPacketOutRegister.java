package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutRegister extends Packet {

    private String name;

    public ClientPacketOutRegister(String name) {
        this.name = name;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.REGISTER.getId());
        serializer.writeString(name);
    }
}
