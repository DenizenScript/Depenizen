package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

public class ClientPacketOutSendPlayer extends Packet {

    private String player;
    private String destination;

    public ClientPacketOutSendPlayer(String player, String destination) {
        this.player = player;
        this.destination = destination;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.SEND_PLAYER.getId());
        serializer.writeString(player);
        serializer.writeString(destination);
    }
}
