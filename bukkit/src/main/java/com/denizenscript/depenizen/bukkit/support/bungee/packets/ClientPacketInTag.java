package com.denizenscript.depenizen.bukkit.support.bungee.packets;

import java.util.Map;

public class ClientPacketInTag extends Packet {

    private int id;
    private String tag;
    private boolean debug;
    private Map<String, String> definitions;
    private String from;

    public ClientPacketInTag() {
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public boolean shouldDebug() {
        return debug;
    }

    public Map<String, String> getDefinitions() {
        return definitions;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        DataDeserializer box = new DataDeserializer(deserializer.readByteArray());
        this.id = box.readInt();
        this.tag = box.readString();
        this.debug = box.readBoolean();
        this.definitions = box.readStringMap();
        this.from = deserializer.readString();
    }
}
