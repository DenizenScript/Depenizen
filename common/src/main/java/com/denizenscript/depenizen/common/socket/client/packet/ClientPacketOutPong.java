package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutPong extends Packet {

    private int bit;

    public ClientPacketOutPong(int bit) {
        this.bit = bit;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.PONG.getId());
        serializer.writeUnsignedByte(bit);
    }
}
