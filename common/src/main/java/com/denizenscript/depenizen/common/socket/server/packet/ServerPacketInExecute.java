package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInExecute extends Packet {

    private String command;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        command = deserializer.readString();
    }

    public String getCommand() {
        return command;
    }
}
