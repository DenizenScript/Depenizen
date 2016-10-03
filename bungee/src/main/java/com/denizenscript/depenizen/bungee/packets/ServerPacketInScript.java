package com.denizenscript.depenizen.bungee.packets;

import java.util.List;

public class ServerPacketInScript extends Packet {

    private List<String> destinations;
    private byte[] scriptData;
    private byte[] definitionsData;

    public ServerPacketInScript() {
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public byte[] getScriptData() {
        return scriptData;
    }

    public byte[] getDefinitionsData() {
        return definitionsData;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.destinations = deserializer.readStringList();
        this.scriptData = deserializer.readByteArray();
        this.definitionsData = deserializer.readByteArray();
    }
}
