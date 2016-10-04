package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;

public class ClientPacketInAcceptRegister extends Packet {

    private boolean accepted;
    private List<String> existingServers;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        accepted = deserializer.readBoolean();
        existingServers = deserializer.readStringList();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public List<String> getExistingServers() {
        return existingServers;
    }
}
