package com.denizenscript.depenizen.bukkit.support.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.security.GeneralSecurityException;
import java.util.List;

public class BukkitSocketClient extends SocketClient {

    public BukkitSocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        super(ipAddress, port, name, password);
    }

    @Override
    public void handleAcceptRegister(String registrationName, List<String> existingServers) {
        dServer.addOnlineServer(registrationName);
        for (String server : existingServers) {
            if (!server.isEmpty()) {
                if (dB.verbose) {
                    dB.log("[Bungee]: Registered with " + server);
                }
                dServer.addOnlineServer(server);
            }
        }
    }

    @Override
    public void handleUpdateServer(String serverName, boolean registered) {
        if (registered) {
            if (dB.verbose) {
                dB.log("[Bungee]: Registered with " + serverName);
            }
            dServer.addOnlineServer(serverName);
        }
        else {
            if (dB.verbose) {
                dB.log("[Bungee]: Disconnected from " + serverName);
            }
            dServer.removeOnlineServer(serverName);
        }
    }
}
