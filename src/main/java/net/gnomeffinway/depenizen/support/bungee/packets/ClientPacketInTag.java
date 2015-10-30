package net.gnomeffinway.depenizen.support.bungee.packets;

public class ClientPacketInTag extends Packet {

    private int id;
    private String tag;
    private String from;

    public ClientPacketInTag() {
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        this.id = box.readInt();
        this.tag = box.readString();

        this.from = deserializer.readString();
    }
}
