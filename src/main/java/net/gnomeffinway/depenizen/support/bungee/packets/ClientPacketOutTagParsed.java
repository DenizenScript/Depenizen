package net.gnomeffinway.depenizen.support.bungee.packets;

public class ClientPacketOutTagParsed extends Packet {

    private int id;
    private String result;
    private String returnToSender;

    public ClientPacketOutTagParsed(int id, String result, String returnToSender) {
        this.id = id;
        this.result = result;
        this.returnToSender = returnToSender;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x06);
        serializer.writeInt(id);
        serializer.writeString(result);
        serializer.writeString(returnToSender);
    }
}
