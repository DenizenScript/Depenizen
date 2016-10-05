package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInTag extends Packet {

    private String destination;
    private byte[] tagData;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        destination = deserializer.readString();
        tagData = deserializer.readByteArray();
    }

    public String getDestination() {
        return destination;
    }

    public byte[] getTagData() {
        return tagData;
    }
}
