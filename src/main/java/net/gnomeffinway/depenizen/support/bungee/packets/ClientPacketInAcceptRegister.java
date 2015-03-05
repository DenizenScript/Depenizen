package net.gnomeffinway.depenizen.support.bungee.packets;

import com.google.common.io.ByteArrayDataInput;

public class ClientPacketInAcceptRegister extends Packet {

    public static enum Action {
        ACCEPTED, DENIED
    }

    private Action action;
    private String[] serverList;

    public ClientPacketInAcceptRegister() {
    }

    public boolean isAccepted() {
        return action == Action.ACCEPTED;
    }

    public String[] getServerList() {
        return serverList;
    }

    @Override
    public void deserialize(ByteArrayDataInput input) {
        this.action = Action.values()[input.readInt()];
        int serverListLength = input.readInt();
        byte[] serverListData = new byte[serverListLength];
        input.readFully(serverListData);
        this.serverList = new String(serverListData).split(", ");
    }
}
