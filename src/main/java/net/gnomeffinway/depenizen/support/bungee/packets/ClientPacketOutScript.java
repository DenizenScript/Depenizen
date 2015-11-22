package net.gnomeffinway.depenizen.support.bungee.packets;

import net.aufdemrand.denizencore.scripts.ScriptEntry;

import java.util.List;
import java.util.Map;

public class ClientPacketOutScript extends Packet {

    private List<String> destinations;
    private boolean shouldDebug;
    private List<ScriptEntry> scriptEntries;
    private Map<String, String> definitions;

    public ClientPacketOutScript(List<String> destinations, boolean shouldDebug, List<ScriptEntry> scriptEntries,
                                 Map<String, String> definitions) {
        this.destinations = destinations;
        this.shouldDebug = shouldDebug;
        this.scriptEntries = scriptEntries;
        this.definitions = definitions;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x02);
        serializer.writeStringList(destinations);

        DataSerializer scriptBox = new DataSerializer();
        scriptBox.writeBoolean(shouldDebug);
        scriptBox.writeInt(scriptEntries.size());
        for (ScriptEntry scriptEntry : scriptEntries) {
            scriptBox.writeString(scriptEntry.getCommandName());
            scriptBox.writeStringList(scriptEntry.getOriginalArguments());
        }
        serializer.writeByteArray(scriptBox.toByteArray());

        DataSerializer definitionsBox = new DataSerializer();
        definitionsBox.writeStringMap(definitions);
        serializer.writeByteArray(definitionsBox.toByteArray());
    }
}
