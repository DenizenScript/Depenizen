package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPingPacketOut extends Packet {

    private int bit;

    public ServerPingPacketOut(int bit) {
        this.bit = bit;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.PING.getId());
        serializer.writeUnsignedByte(bit);
    }
}
