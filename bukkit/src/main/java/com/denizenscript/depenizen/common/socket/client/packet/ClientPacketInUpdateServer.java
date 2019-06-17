package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketInUpdateServer extends Packet {

    private String name;
    private boolean registered;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        name = deserializer.readString();
        registered = deserializer.readBoolean();
    }

    public String getName() {
        return name;
    }

    public boolean isRegistered() {
        return registered;
    }
}
