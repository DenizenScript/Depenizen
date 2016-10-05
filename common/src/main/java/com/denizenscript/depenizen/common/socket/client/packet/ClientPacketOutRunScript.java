package com.denizenscript.depenizen.common.socket.client.packet;

import com.denizenscript.depenizen.common.socket.DataSerializer;
import com.denizenscript.depenizen.common.socket.Packet;

import java.util.List;
import java.util.Map;

public class ClientPacketOutRunScript extends Packet {

    private List<String> destinations;
    private String scriptName;
    private Map<String, String> definitions;
    private boolean shouldDebug;

    public ClientPacketOutRunScript(List<String> destinations, String scriptName, Map<String, String> definitions, boolean shouldDebug) {
        this.destinations = destinations;
        this.scriptName = scriptName;
        this.definitions = definitions;
        this.shouldDebug = shouldDebug;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeUnsignedByte(ServerBound.RUN_SCRIPT.getId());
        serializer.writeStringList(destinations);
        DataSerializer box = new DataSerializer();
        box.writeString(scriptName);
        box.writeStringMap(definitions);
        box.writeBoolean(shouldDebug);
        serializer.writeByteArray(box.toByteArray());
    }
}
