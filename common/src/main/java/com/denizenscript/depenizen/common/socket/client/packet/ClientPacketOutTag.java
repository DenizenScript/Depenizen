package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketOutTag extends Packet {

    private String destination;
    private int id;
    private String tag;
    private boolean fullDebug;
    private boolean minimalDebug;
    private Map<String, String> definitions;

    public ClientPacketOutTag(String destination, int id, String tag, boolean fullDebug, boolean minimalDebug, Map<String, String> definitions) {
        this.destination = destination;
        this.id = id;
        this.tag = tag;
        this.fullDebug = fullDebug;
        this.minimalDebug = minimalDebug;
        this.definitions = definitions;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.TAG.getId());
        serializer.writeString(destination);
        DataSerializer box = new DataSerializer();
        box.writeInt(id);
        box.writeString(tag);
        box.writeBoolean(fullDebug);
        box.writeBoolean(minimalDebug);
        box.writeStringMap(definitions);
        serializer.writeByteArray(box.toByteArray());
    }
}
