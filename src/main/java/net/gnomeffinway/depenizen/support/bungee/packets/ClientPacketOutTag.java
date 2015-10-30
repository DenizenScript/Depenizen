package net.gnomeffinway.depenizen.support.bungee.packets;

public class ClientPacketOutTag extends Packet {

    private int id;
    private String tag;
    private String destination;

    public ClientPacketOutTag(int id, String tag, String destination) {
        this.id = id;
        this.tag = tag;
        this.destination = destination;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x05);

        DataSerializer box = new DataSerializer();
        box.writeInt(id);
        box.writeString(tag);
        serializer.writeByteArray(box.toByteArray());

        serializer.writeString(destination);
    }
}
