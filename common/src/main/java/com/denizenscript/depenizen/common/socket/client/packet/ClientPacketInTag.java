package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketInTag extends Packet {

    private String from;
    private int id;
    private String tag;
    private boolean debug;
    private Map<String, String> definitions;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        from = deserializer.readString();
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        id = box.readInt();
        tag = box.readString();
        debug = box.readBoolean();
        definitions = box.readStringMap();
    }

    public String getFrom() {
        return from;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public boolean shouldDebug() {
        return debug;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }
}
