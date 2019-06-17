package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutPing extends Packet {

    private int bit;

    public ClientPacketOutPing(int bit) {
        this.bit = bit;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.PING.getId());
        serializer.writeUnsignedByte(bit);
    }
}
