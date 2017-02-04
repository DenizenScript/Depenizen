package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;

public class ServerPacketInSetPriority extends Packet {

    private List<String> prioritylist;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        prioritylist = deserializer.readStringList();
    }

    public List<String> getPriorityList() {
        return prioritylist;
    }
}
