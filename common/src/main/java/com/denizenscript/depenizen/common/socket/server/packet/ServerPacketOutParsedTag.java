package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutParsedTag extends Packet {

    private byte[] resultData;

    public ServerPacketOutParsedTag(byte[] resultData) {
        this.resultData = resultData;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.PARSED_TAG.getId());
        serializer.writeByteArray(resultData);
    }
}
