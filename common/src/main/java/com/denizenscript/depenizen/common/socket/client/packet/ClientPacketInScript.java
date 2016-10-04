package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;
import java.util.Map;

public class ClientPacketInScript extends Packet {

    private boolean shouldDebug;
    private Map<String, List<String>> scriptEntries;
    private Map<String, String> definitions;

    public boolean shouldDebug() {
        return shouldDebug;
    }

    public Map<String, List<String>> getScriptEntries() {
        return scriptEntries;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        shouldDebug = box.readBoolean();
        scriptEntries = box.readStringListMap();
        definitions = box.readStringMap();
    }
}
