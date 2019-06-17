package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutTag extends Packet {

    private String from;
    private byte[] tagData;

    public ServerPacketOutTag(String from, byte[] tagData) {
        this.from = from;
        this.tagData = tagData;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.TAG.getId());
        serializer.writeString(from);
        serializer.writeByteArray(tagData);
    }
}
