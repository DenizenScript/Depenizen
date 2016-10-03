package com.denizenscript.depenizen.bukkit.support.bungee.packets;

public class ClientPacketInTagParsed extends Packet {

    private int id;
    private String result;

    public ClientPacketInTagParsed() {
    }

    public int getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        this.id = box.readInt();
        this.result = box.readString();
    }
}
