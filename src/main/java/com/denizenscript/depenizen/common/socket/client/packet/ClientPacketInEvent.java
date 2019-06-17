package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketInEvent extends Packet {

    private long id;
    private String event;
    private Map<String, String> context;
    private boolean shouldRespond;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        id = deserializer.readLong();
        event = deserializer.readString();
        context = deserializer.readStringMap();
        shouldRespond = deserializer.readBoolean();
    }

    public long getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public boolean shouldRespond() {
        return shouldRespond;
    }
}
