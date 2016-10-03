package com.denizenscript.depenizen.bungee.packets;

public class ServerPacketOutScript extends Packet {

    private byte[] scriptData;
    private byte[] definitionsData;

    public ServerPacketOutScript(byte[] scriptData, byte[] definitionsData) {
        this.scriptData = scriptData;
        this.definitionsData = definitionsData;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x02);
        serializer.writeByteArray(scriptData);
        serializer.writeByteArray(definitionsData);
    }
}
