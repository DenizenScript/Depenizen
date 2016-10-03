package com.morphanone.depenizenbungee.packets;

import java.util.Map;

public class ServerPacketInEventResponse extends Packet {

    private long eventId;
    private Map<String, String> determinations;

    public ServerPacketInEventResponse() {
    }

    public long getEventId() {
        return eventId;
    }

    public Map<String, String> getDeterminations() {
        return determinations;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.eventId = deserializer.readLong();
        this.determinations = deserializer.readStringMap();
    }
}
