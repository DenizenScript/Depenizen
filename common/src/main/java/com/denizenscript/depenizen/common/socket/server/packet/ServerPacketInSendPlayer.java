package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ServerPacketInSendPlayer extends Packet {

    private String player;
    private String destination;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        player = deserializer.readString();
        destination = deserializer.readString();
    }

    public String getPlayer() {
        return player;
    }

    public String getDestination() {
        return destination;
    }
}
