package net.gnomeffinway.depenizen.support.bungee;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
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

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("bungee")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <bungee.server>
            // @returns dServer
            // @description
            // Returns the current server as a BungeeCord dServer.
            // @plugin Depenizen, BungeeCord
            // -->
            if (attribute.startsWith("server")) {
                dServer server = dServer.getServerFromName(Settings.socketName());
                if (server != null) {
                    return server.getAttribute(attribute.fulfill(1));
                }
                dB.echoError("Current server not registered!");
            }

            // <--[tag]
            // @attribute <bungee.list_servers>
            // @returns dList(dServer)
            // @description
            // Lists all servers connected to the BungeeCord network.
            // @plugin Depenizen, BungeeCord
            // -->
            if (attribute.startsWith("list_servers")) {
                dList list = new dList();
                for (dServer server : dServer.getOnlineServers().values()) {
                    list.add(server.identify());
                }
                return list.getAttribute(attribute.fulfill(1));
            }
        }

        return null;

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
