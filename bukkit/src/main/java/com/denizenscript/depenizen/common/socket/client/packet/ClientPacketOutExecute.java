package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutExecute extends Packet {

    private String command;

    public ClientPacketOutExecute(String command) {
        this.command = command;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(Packet.ServerBound.EXECUTE.getId());
        serializer.writeString(command);
    }
}
