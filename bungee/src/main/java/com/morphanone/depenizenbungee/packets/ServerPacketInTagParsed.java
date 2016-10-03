package com.morphanone.depenizenbungee.packets;

public class ServerPacketInTagParsed extends Packet {

    private byte[] box;
    private String returnToSender;

    public byte[] getBox() {
        return box;
    }

    public String getReturnToSender() {
        return returnToSender;
    }

    public ServerPacketInTagParsed() {
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.box = deserializer.readByteArray();
        this.returnToSender = deserializer.readString();
    }
}
