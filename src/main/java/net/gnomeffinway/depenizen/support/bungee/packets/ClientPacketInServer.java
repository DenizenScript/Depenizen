package net.gnomeffinway.depenizen.support.bungee.packets;

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
    public void deserialize(DataDeserializer deserializer) {
        this.action = Action.values()[deserializer.readInt()];
        this.name = deserializer.readString();
    }
}
