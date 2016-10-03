package com.morphanone.depenizenbungee.packets;

public class ServerPacketInEventSubscribe extends Packet {

    public static enum Action {
        SUBSCRIBE, UNSUBSCRIBE
    }

    private Action action;
    private String event;

    public ServerPacketInEventSubscribe() {
    }

    public boolean isSubscribed() {
        return action == Action.SUBSCRIBE;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.action = Action.values()[deserializer.readInt()];
        this.event = deserializer.readString();
    }
}
