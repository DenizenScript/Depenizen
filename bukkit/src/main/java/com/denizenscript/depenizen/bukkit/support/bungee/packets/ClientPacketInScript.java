package com.denizenscript.depenizen.bukkit.support.bungee.packets;

import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientPacketInScript extends Packet {

    private List<ScriptEntry> scriptEntries;
    private Map<String, String> definitions;

    public ClientPacketInScript() {
    }

    public List<ScriptEntry> getScriptEntries() {
        return scriptEntries;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer scriptBox = new DataDeserializer(deserializer.readByteArray());

        List<ScriptEntry> scriptEntryList = new ArrayList<ScriptEntry>();
        boolean shouldDebug = scriptBox.readBoolean();
        int commandCount = scriptBox.readInt();
        try {
            for (int i = 0; i < commandCount; i++) {
                String commandName = scriptBox.readString();
                String[] arguments = scriptBox.readStringArray();
                ScriptEntry scriptEntry = new ScriptEntry(commandName, arguments, null);
                scriptEntry.fallbackDebug = shouldDebug;
                scriptEntryList.add(scriptEntry);
            }
        }
        catch (Exception e) {
            dB.echoError(e);
            return;
        }
        this.scriptEntries = scriptEntryList;

        DataDeserializer definitionsBox = new DataDeserializer(deserializer.readByteArray());
        this.definitions = definitionsBox.readStringMap();
    }
}
