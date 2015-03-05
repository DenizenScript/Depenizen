package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataInput;

public class ClientPacketInServer extends Packet {

    public static enum Action {
        REGISTERED, DISCONNECTED
    }

    private Action action;
    private String name;

    public ClientPacketInServer() {
    }

    public String getServerName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public void deserialize(ByteArrayDataInput input) {
        this.action = Action.values()[input.readInt()];
        int nameLength = input.readInt();
        byte[] nameData = new byte[nameLength];
        input.readFully(nameData);
        this.name = new String(nameData);
    }
}
