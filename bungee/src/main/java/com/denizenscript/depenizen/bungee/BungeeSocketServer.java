package com.denizenscript.depenizen.bungee;

import com.denizenscript.depenizen.common.socket.server.ClientConnection;
import com.denizenscript.depenizen.common.socket.server.SocketServer;

import java.security.GeneralSecurityException;
import java.util.Map;

public class BungeeSocketServer extends SocketServer {

    public BungeeSocketServer(int port, int maxClients, char[] password) throws GeneralSecurityException {
        super(port, maxClients, password);
    }

    @Override
    protected void handleEventSubscription(ClientConnection client, String event, boolean subscribed) {
        if (subscribed) {
            EventManager.subscribe(event, client);
        }
        else {
            EventManager.unsubscribe(event, client);
        }
    }

    @Override
    protected void handleEventResponse(ClientConnection client, long id, Map<String, String> map) {
        EventManager.respond(id, map);
    }
}
