package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Set;

public class ServerPacketOutAcceptRegister extends Packet {

    private boolean accepted;
    private Set<String> existingServers;

    public ServerPacketOutAcceptRegister(boolean accepted, Set<String> existingServers) {
        this.accepted = accepted;
        this.existingServers = existingServers;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.ACCEPT_REGISTER.getId());
        serializer.writeBoolean(accepted);
        serializer.writeStringList(existingServers);
    }
}
