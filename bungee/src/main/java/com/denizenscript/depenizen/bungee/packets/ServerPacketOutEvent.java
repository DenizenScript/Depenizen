package com.denizenscript.depenizen.bungee.packets;

import java.util.Map;

public class ServerPacketOutEvent extends Packet {

    private boolean getResponse;
    private long id;
    private String name;
    private Map<String, String> context;

    public ServerPacketOutEvent(boolean getResponse, long id, String name, Map<String, String> context) {
        this.getResponse = getResponse;
        this.id = id;
        this.name = name;
        this.context = context;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x03);
        serializer.writeBoolean(getResponse);
        serializer.writeLong(id);
        serializer.writeString(name);
        serializer.writeStringMap(context);
    }
}
