package com.denizenscript.depenizen.common.socket.server.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ServerPacketOutEvent extends Packet {

    private long id;
    private String event;
    private Map<String, String> context;
    private boolean getResponse;

    public ServerPacketOutEvent(long id, String event, Map<String, String> context, boolean getResponse) {
        this.id = id;
        this.event = event;
        this.context = context;
        this.getResponse = getResponse;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ClientBound.EVENT.getId());
        serializer.writeLong(id);
        serializer.writeString(event);
        serializer.writeStringMap(context);
        serializer.writeBoolean(getResponse);
    }
}
