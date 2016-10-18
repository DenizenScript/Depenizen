package com.denizenscript.depenizen.bungee;

import com.denizenscript.depenizen.common.socket.server.ClientConnection;
import com.denizenscript.depenizen.common.socket.server.SocketServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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

    @Override
    protected void handleSendPlayer(ClientConnection client, String player, String destination) {
        ProxiedPlayer bungeePlayer = ProxyServer.getInstance().getPlayer(player);
        if (bungeePlayer != null) {
            ServerInfo server = ProxyServer.getInstance().getServerInfo(destination);
            if (server != null) {
                bungeePlayer.connect(server);
            }
        }
    }
}
