package net.gnomeffinway.depenizen.support.bungee;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.gnomeffinway.depenizen.Settings;
import net.gnomeffinway.depenizen.commands.BungeeCommand;
import net.gnomeffinway.depenizen.events.bungee.ProxyPingScriptEvent;
import net.gnomeffinway.depenizen.extensions.bungee.BungeePlayerExtension;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.Support;

public class BungeeSupport extends Support {

    public BungeeSupport() {
        new BungeeCommand().activate().as("BUNGEE").withOptions("bungee", 2);
        registerObjects(dServer.class);
        registerProperty(BungeePlayerExtension.class, dPlayer.class);
        ScriptEvent.registerScriptEvent(new ProxyPingScriptEvent());
        startSocket();
    }

    ////////////////////////////////
    //// Socket Connection Manager
    //////////////////////

    private static SocketClient socketClient;

    public static void startSocket() {
        if (Settings.socketEnabled()) {
            String ipAddress = Settings.socketIpAddress();
            if (ipAddress == null) {
                dB.echoError("BungeeCord Socket is enabled, but no IP address is specified.");
                return;
            }
            String password = Settings.socketPassword();
            if (password == null) {
                dB.echoError("BungeeCord Socket is enabled, but no password is specified.");
                return;
            }
            String name = Settings.socketName();
            if (name == null) {
                dB.echoError("BungeeCord Socket is enabled, but no registration name is specified.");
                return;
            }
            socketClient = new SocketClient(ipAddress, Settings.socketPort(),
                    password, name, Settings.socketTimeout());
            socketClient.connect();
        }
    }

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    public static boolean isSocketConnected() {
        return socketClient != null && socketClient.isConnected();
    }

    public static void closeSocket() {
        if (socketClient != null) {
            socketClient.close();
        }
    }
}
