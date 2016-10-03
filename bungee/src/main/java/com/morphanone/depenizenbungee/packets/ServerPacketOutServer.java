package com.morphanone.depenizenbungee.packets;

public class ServerPacketOutServer extends Packet {

    public static enum Action {
        REGISTERED, DISCONNECTED
    }

    private Action action;
    private String name;

    public ServerPacketOutServer(Action action, String name) {
        this.action = action;
        this.name = name;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x01);
        serializer.writeInt(action.ordinal());
        serializer.writeString(name);
    }
}
