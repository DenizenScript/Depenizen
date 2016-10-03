package com.morphanone.depenizenbungee.packets;

public abstract class Packet {

    public void serialize(DataSerializer serializer) {
        throw new UnsupportedOperationException("Cannot serialize an inbound packet.");
    }

    public void deserialize(DataDeserializer deserializer) {
        throw new UnsupportedOperationException("Cannot deserialize an outbound packet.");
    }
}
