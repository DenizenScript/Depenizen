package com.denizenscript.depenizen.bukkit.support.bungee.packets;

import java.util.Map;

public class ClientPacketInEvent extends Packet {

    private boolean sendResponse;
    private long eventId;
    private String eventName;
    private Map<String, String> context;

    public ClientPacketInEvent() {
    }

    public boolean shouldSendResponse() {
        return sendResponse;
    }

    public long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Map<String, String> getContext() {
        return context;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.sendResponse = deserializer.readBoolean();
        this.eventId = deserializer.readLong();
        this.eventName = deserializer.readString();
        this.context = deserializer.readStringMap();
    }
}
