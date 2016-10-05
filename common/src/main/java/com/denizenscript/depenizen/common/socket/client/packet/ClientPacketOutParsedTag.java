package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutParsedTag extends Packet {

    private String destination;
    private int id;
    private String result;

    public ClientPacketOutParsedTag(String destination, int id, String result) {
        this.destination = destination;
        this.id = id;
        this.result = result;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.PARSED_TAG.getId());
        serializer.writeString(destination);
        DataSerializer box = new DataSerializer();
        box.writeInt(id);
        box.writeString(result);
        serializer.writeByteArray(box.toByteArray());
    }
}
