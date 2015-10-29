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
        serializer.writeInt(id);
        serializer.writeString(tag);
        serializer.writeString(destination);
    }
}
