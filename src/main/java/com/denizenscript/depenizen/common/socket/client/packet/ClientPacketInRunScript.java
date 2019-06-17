package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.Map;

public class ClientPacketInRunScript extends Packet {

    private String scriptName;
    private Map<String, String> definitions;
    private boolean fullDebug;
    private boolean minimalDebug;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        scriptName = box.readString();
        definitions = box.readStringMap();
        fullDebug = box.readBoolean();
        minimalDebug = box.readBoolean();
    }

    public String getScriptName() {
        return scriptName;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    public boolean showFullDebug() {
        return fullDebug;
    }

    public boolean showMinimalDebug() {
        return minimalDebug;
    }
}
