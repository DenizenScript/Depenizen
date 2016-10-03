package com.morphanone.depenizenbungee.packets;

public class ServerPacketInRegister extends Packet {

    private String name;

    public ServerPacketInRegister() {
    }

    public String getName() {
        return name;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.name = deserializer.readString();
    }
}
