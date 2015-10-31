package net.gnomeffinway.depenizen.support.bungee.packets;

import java.util.Map;

public class ClientPacketOutTag extends Packet {

    private int id;
    private String tag;
    private boolean debug;
    private Map<String, String> definitions;
    private String destination;

    public ClientPacketOutTag(int id, String tag, boolean debug, Map<String, String> definitions, String destination) {
        this.id = id;
        this.tag = tag;
        this.debug = debug;
        this.definitions = definitions;
        this.destination = destination;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x05);

        DataSerializer box = new DataSerializer();
        box.writeInt(id);
        box.writeString(tag);
        box.writeBoolean(debug);
        box.writeStringMap(definitions);
        serializer.writeByteArray(box.toByteArray());

        serializer.writeString(destination);
    }
}
