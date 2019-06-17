package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutRunScript extends Packet {

    private byte[] scriptData;

    public ServerPacketOutRunScript(byte[] scriptData) {
        this.scriptData = scriptData;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.RUN_SCRIPT.getId());
        serializer.writeByteArray(scriptData);
    }
}
