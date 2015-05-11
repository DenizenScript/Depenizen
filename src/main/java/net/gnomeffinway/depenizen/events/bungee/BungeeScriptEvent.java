package net.gnomeffinway.depenizen.events.bungee;

import net.aufdemrand.denizencore.events.ScriptEvent;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.support.bungee.SocketClient;
import net.gnomeffinway.depenizen.support.bungee.packets.ClientPacketOutEventSubscribe;

public abstract class BungeeScriptEvent extends ScriptEvent {

    @Override
    public void init() {
        SocketClient socketClient = Depenizen.getCurrentInstance().getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            socketClient.send(new ClientPacketOutEventSubscribe(ClientPacketOutEventSubscribe.Action.SUBSCRIBE, getName()));
        }
    }

    @Override
    public void destroy() {
        SocketClient socketClient = Depenizen.getCurrentInstance().getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            socketClient.send(new ClientPacketOutEventSubscribe(ClientPacketOutEventSubscribe.Action.UNSUBSCRIBE, getName()));
        }
    }
}
