package com.denizenscript.depenizen.bungee.packets;

public class ServerPacketOutTag extends Packet {

    private byte[] box;
    private String from;

    public ServerPacketOutTag(byte[] box, String from) {
        this.box = box;
        this.from = from;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x05);
        serializer.writeByteArray(box);
        serializer.writeString(from);
    }
}
