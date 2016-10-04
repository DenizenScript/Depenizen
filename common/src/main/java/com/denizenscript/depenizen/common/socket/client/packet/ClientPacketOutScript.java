package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;
import java.util.Map;

public class ClientPacketOutScript extends Packet {

    private List<String> destinations;
    private boolean shouldDebug;
    private Map<String, List<String>> scriptEntries;
    private Map<String, String> definitions;

    public ClientPacketOutScript(List<String> destinations, boolean shouldDebug, Map<String, List<String>> scriptEntries,
                                 Map<String, String> definitions) {
        this.destinations = destinations;
        this.shouldDebug = shouldDebug;
        this.scriptEntries = scriptEntries;
        this.definitions = definitions;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.SCRIPT.getId());
        serializer.writeStringList(destinations);
        DataSerializer box = new DataSerializer();
        box.writeBoolean(shouldDebug);
        box.writeStringListMap(scriptEntries);
        box.writeStringMap(definitions);
        serializer.writeByteArray(box.toByteArray());
    }
}
