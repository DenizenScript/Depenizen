package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ServerPacketInEventResponse extends Packet {

    private long id;
    private Map<String, String> response;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        id = deserializer.readLong();
        response = deserializer.readStringMap();
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getResponse() {
        return response;
    }
}
