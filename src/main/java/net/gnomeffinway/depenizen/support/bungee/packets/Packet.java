package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public abstract class Packet {

    public void serialize(ByteArrayDataOutput output) {
        throw new UnsupportedOperationException("Cannot serialize an inbound packet.");
    }

    public void deserialize(ByteArrayDataInput input) {
        throw new UnsupportedOperationException("Cannot deserialize an outbound packet.");
    }
}
