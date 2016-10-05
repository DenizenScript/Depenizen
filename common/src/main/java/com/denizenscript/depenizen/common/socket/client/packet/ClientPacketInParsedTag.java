package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketInParsedTag extends Packet {

    private int id;
    private String result;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        id = box.readInt();
        result = box.readString();
    }

    public int getId() {
        return id;
    }

    public String getResult() {
        return result;
    }
}
