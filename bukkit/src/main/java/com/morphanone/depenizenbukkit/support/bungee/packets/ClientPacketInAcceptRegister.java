package com.morphanone.depenizenbukkit.support.bungee.packets;

import java.util.List;

public class ClientPacketInAcceptRegister extends Packet {

    public static enum Action {
        ACCEPTED, DENIED
    }

    private Action action;
    private List<String> serverList;

    public ClientPacketInAcceptRegister() {
    }

    public boolean isAccepted() {
        return action == Action.ACCEPTED;
    }

    public List<String> getServerList() {
        return serverList;
    }

    @Override
    public void deserialize(DataDeserializer deserializer) {
        this.action = Action.values()[deserializer.readInt()];
        this.serverList = deserializer.readStringList();
    }
}
