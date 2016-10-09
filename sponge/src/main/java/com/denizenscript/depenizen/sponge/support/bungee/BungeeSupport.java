package com.denizenscript.depenizen.sponge.support.bungee;

import com.denizenscript.depenizen.common.socket.client.SocketClient;
import com.denizenscript.depenizen.sponge.Depenizen2Sponge;
import com.denizenscript.depenizen.sponge.Settings;

import java.io.IOException;

public class BungeeSupport {

    private static SocketClient socketClient;

    public static void startSocket() {
        if (Settings.socketEnabled()) {
            String ipAddress = Settings.socketIpAddress();
            if (ipAddress == null) {
                Depenizen2Sponge.instance.debugError("BungeeCord Socket is enabled, but no IP address is specified.");
                return;
            }
            String password = Settings.socketPassword();
            if (password == null) {
                Depenizen2Sponge.instance.debugError("BungeeCord Socket is enabled, but no password is specified.");
                return;
            }
            String name = Settings.socketName();
            if (name == null) {
                Depenizen2Sponge.instance.debugError("BungeeCord Socket is enabled, but no registration name is specified.");
                return;
            }
            try {
                socketClient = new SpongeSocketClient(ipAddress, Settings.socketPort(), name, password.toCharArray());
                socketClient.connect();
            }
            catch (IOException e) {
                Depenizen2Sponge.instance.debugError("BungeeCord Socket is not online.");
                socketClient.attemptReconnect();
            }
            catch (Exception e) {
                Depenizen2Sponge.instance.debugError("BungeeCord Socket failed to connect due to an exception.");
                Depenizen2Sponge.instance.debugException(e);
            }
        }
    }

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    public static boolean isSocketConnected() {
        return socketClient != null && socketClient.isConnected();
    }

    public static boolean isSocketRegistered() {
        return isSocketConnected() && socketClient.isRegistered();
    }

    public static boolean isSocketReconnecting() {
        return socketClient != null && socketClient.isReconnecting();
    }

    public static void attemptReconnect() {
        socketClient.attemptReconnect();
    }

    public static void closeSocket() {
        if (isSocketConnected()) {
            socketClient.close("Closed.", false);
        }
    }
}
