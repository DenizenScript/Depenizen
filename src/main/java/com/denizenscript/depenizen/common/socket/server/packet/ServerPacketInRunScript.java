package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;

public class ServerPacketInRunScript extends Packet {

    private List<String> destinations;
    private byte[] scriptData;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        destinations = deserializer.readStringList();
        scriptData = deserializer.readByteArray();
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public byte[] getScriptData() {
        return scriptData;
    }
}
