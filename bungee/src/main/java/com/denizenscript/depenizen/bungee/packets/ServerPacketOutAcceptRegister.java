package com.denizenscript.depenizen.bungee.packets;

import java.util.Set;

public class ServerPacketOutAcceptRegister extends Packet {

    public static enum Action {
        ACCEPTED, DENIED
    }

    private Action action;
    private Set<String> serverList;

    public ServerPacketOutAcceptRegister(Action action, Set<String> serverList) {
        this.action = action;
        this.serverList = serverList;
    }

    @Override
    public void serialize(DataSerializer serializer) {
        serializer.writeInt(0x00);
        serializer.writeInt(action.ordinal());
        serializer.writeStringList(serverList);
    }
}
