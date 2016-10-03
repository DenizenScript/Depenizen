package com.morphanone.depenizenbungee.packets;

public class ServerPacketOutTagParsed extends Packet {

    private byte[] box;

    public ServerPacketOutTagParsed(byte[] box) {
        this.box = box;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x06);
        serializer.writeByteArray(box);
    }
}
