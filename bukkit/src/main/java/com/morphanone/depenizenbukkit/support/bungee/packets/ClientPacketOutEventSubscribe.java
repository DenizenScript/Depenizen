package com.morphanone.depenizenbukkit.support.bungee.packets;

public class ClientPacketOutEventSubscribe extends Packet {

    public static enum Action {
        SUBSCRIBE, UNSUBSCRIBE
    }

    private Action action;
    private String event;

    public ClientPacketOutEventSubscribe(Action action, String event) {
        this.action = action;
        this.event = event;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x04);
        serializer.writeInt(action.ordinal());
        serializer.writeString(event);
    }
}
