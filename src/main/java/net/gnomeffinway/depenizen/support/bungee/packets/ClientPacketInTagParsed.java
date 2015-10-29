package net.gnomeffinway.depenizen.support.bungee.packets;

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
        this.id = deserializer.readInt();
        this.result = deserializer.readString();
    }
}
