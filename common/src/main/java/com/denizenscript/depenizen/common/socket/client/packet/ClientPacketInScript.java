package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.util.SimpleScriptEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientPacketInScript extends Packet {

    private boolean shouldDebug;
    private List<SimpleScriptEntry> scriptEntries;
    private Map<String, String> definitions;

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        shouldDebug = box.readBoolean();
        scriptEntries = new ArrayList<SimpleScriptEntry>();
        int commandCount = box.readInt();
        for (int i = 0; i < commandCount; i++) {
            String command = box.readString();
            List<String> arguments = box.readStringList();
            scriptEntries.add(new SimpleScriptEntry(command, arguments));
        }
        definitions = box.readStringMap();
    }

    public boolean shouldDebug() {
        return shouldDebug;
    }

    public List<SimpleScriptEntry> getScriptEntries() {
        return scriptEntries;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }
}
