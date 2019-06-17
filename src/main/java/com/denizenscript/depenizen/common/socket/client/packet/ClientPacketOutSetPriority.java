package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;

public class ClientPacketOutSetPriority extends Packet {

    private List<String> prioritylist;

    public ClientPacketOutSetPriority(List<String> prioritylist) {
        this.prioritylist = prioritylist;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.SET_PRIORITY.getId());
        serializer.writeStringList(prioritylist);
    }
}
