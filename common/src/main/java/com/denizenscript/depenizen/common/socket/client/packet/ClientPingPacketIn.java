package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPingPacketIn extends Packet {

    private int bit;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        bit = deserializer.readUnsignedByte();
    }

    public int getBit() {
        return bit;
    }
}
