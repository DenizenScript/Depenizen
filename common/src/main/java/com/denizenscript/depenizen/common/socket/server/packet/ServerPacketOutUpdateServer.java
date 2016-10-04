package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketOutUpdateServer extends Packet {

    private String name;
    private boolean registered;

    public ServerPacketOutUpdateServer(String name, boolean registered) {
        this.name = name;
        this.registered = registered;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.UPDATE_SERVER.getId());
        serializer.writeString(name);
        serializer.writeBoolean(registered);
    }
}
