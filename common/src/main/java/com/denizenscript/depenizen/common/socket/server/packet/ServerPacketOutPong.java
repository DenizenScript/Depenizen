package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutPong extends Packet {

    private int bit;

    public ServerPacketOutPong(int bit) {
        this.bit = bit;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.PONG.getId());
        serializer.writeUnsignedByte(bit);
    }
}
