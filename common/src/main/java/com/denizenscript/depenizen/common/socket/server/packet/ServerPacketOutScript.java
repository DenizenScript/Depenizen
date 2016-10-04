package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutScript extends Packet {

    private byte[] scriptData;

    public ServerPacketOutScript(byte[] scriptData) {
        this.scriptData = scriptData;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.SCRIPT.getId());
        serializer.writeByteArray(scriptData);
    }
}
