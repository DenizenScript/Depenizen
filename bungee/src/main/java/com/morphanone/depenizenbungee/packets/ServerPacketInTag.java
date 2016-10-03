package com.morphanone.depenizenbungee.packets;

public class ServerPacketInTag extends Packet {

    private byte[] box;
    private String server;

    public byte[] getBox() {
        return box;
    }

    public String getServer() {
        return server;
    }

    public ServerPacketInTag() {
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.box = deserializer.readByteArray();
        this.server = deserializer.readString();
    }
}
