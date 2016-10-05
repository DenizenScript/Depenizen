package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInEventSubscription extends Packet {

    private String event;
    private boolean subscribed;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        event = deserializer.readString();
        subscribed = deserializer.readBoolean();
    }

    public String getEvent() {
        return event;
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
