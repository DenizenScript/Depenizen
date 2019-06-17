package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketOutEventResponse extends Packet {

    private long id;
    private Map<String, String> response;

    public ClientPacketOutEventResponse(long id, Map<String, String> response) {
        this.id = id;
        this.response = response;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.EVENT_RESPONSE.getId());
        serializer.writeLong(id);
        serializer.writeStringMap(response);
    }
}
