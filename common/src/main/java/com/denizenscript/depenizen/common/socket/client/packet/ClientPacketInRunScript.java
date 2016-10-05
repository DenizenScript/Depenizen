package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketInRunScript extends Packet {

    private String scriptName;
    private Map<String, String> definitions;
    private boolean shouldDebug;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        scriptName = box.readString();
        definitions = box.readStringMap();
        shouldDebug = box.readBoolean();
    }

    public String getScriptName() {
        return scriptName;
    }

    public boolean shouldDebug() {
        return shouldDebug;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }
}
