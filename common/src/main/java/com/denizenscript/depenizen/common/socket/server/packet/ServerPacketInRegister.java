package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInRegister extends Packet {

    private String name;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.name = deserializer.readString();
    }

    public String getName() {
        return name;
    }
}
