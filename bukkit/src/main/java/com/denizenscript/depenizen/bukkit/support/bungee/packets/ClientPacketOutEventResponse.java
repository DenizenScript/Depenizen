package com.denizenscript.depenizen.bukkit.support.bungee.packets;

import java.util.Map;

public class ClientPacketOutEventResponse extends Packet {

    private long eventId;
    private Map<String, String> determinations;

    public ClientPacketOutEventResponse(long eventId, Map<String, String> determinations) {
        this.eventId = eventId;
        this.determinations = determinations;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x03);
        serializer.writeLong(eventId);
        serializer.writeStringMap(determinations);
    }
}
