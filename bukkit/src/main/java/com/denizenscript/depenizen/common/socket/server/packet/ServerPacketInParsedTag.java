package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInParsedTag extends Packet {

    private String destination;
    private byte[] resultData;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        destination = deserializer.readString();
        resultData = deserializer.readByteArray();
    }

    public String getDestination() {
        return destination;
    }

    public byte[] getResultData() {
        return resultData;
    }
}
