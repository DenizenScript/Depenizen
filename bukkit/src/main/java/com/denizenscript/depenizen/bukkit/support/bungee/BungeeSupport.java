package com.denizenscript.depenizen.bukkit.support.bungee;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.Settings;
import com.denizenscript.depenizen.bukkit.commands.bungee.*;
import com.denizenscript.depenizen.bukkit.events.bungee.*;
import com.denizenscript.depenizen.bukkit.extensions.bungee.BungeePlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import org.bukkit.Bukkit;

import java.io.IOException;

public class BungeeSupport extends Support {

    public BungeeSupport() {
        new BungeeCommand().activate().as("BUNGEE").withOptions("bungee", 1);
        new BungeeRunCommand().activate().as("BUNGEERUN").withOptions("bungeerun [<server>|...] [<script_name>]", 2);
        new BungeeTagCommand().activate().as("BUNGEETAG").withOptions("bungeetag [<tag>] [server:<server>]", 2);
        new BungeeReconnectCommand().activate().as("BUNGEERECONNECT").withOptions("bungeereconnect", 0);
        registerObjects(dServer.class);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(DepenizenPlugin.getCurrentInstance(), "BungeeCord");
        registerProperty(BungeePlayerExtension.class, dPlayer.class);
        registerAdditionalTags("bungee");
        registerScriptEvents(new BungeeServerConnectScriptEvent());
        registerScriptEvents(new BungeeServerDisconnectScriptEvent());
        registerScriptEvents(new ProxyPingScriptEvent());
        registerScriptEvents(new PostLoginScriptEvent());
        registerScriptEvents(new PlayerDisconnectScriptEvent());
        registerScriptEvents(new ReconnectFailScriptEvent());
        registerScriptEvents(new ServerSwitchScriptEvent());
        startSocket();
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {

        if (attribute.startsWith("bungee")) {

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <bungee.server>
            // @returns dServer
            // @description
            // Returns the current server as a BungeeCord dServer.
            // @Plugin DepenizenBukkit, DepenizenBungee
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
            // @Plugin DepenizenBukkit, DepenizenBungee
            // -->
            if (attribute.startsWith("list_servers")) {
                dList list = new dList();
                for (dServer server : dServer.getOnlineServers().values()) {
                    list.add(server.identify());
                }
                return list.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <bungee.connected>
            // @returns Element(Boolean)
            // @description
            // Returns whether the server is connected to the BungeeCord socket.
            // @Plugin DepenizenBukkit, DepenizenBungee
            // -->
            if (attribute.startsWith("connected")) {
                return new Element(isSocketConnected()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <bungee.reconnecting>
            // @returns Element(Boolean)
            // @description
            // Returns whether the server is trying to reconnect to the BungeeCord socket.
            // @Plugin DepenizenBukkit, DepenizenBungee
            // -->
            if (attribute.startsWith("reconnecting")) {
                return new Element(isSocketReconnecting()).getAttribute(attribute.fulfill(1));
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
            try {
                socketClient = new BukkitSocketClient(ipAddress, Settings.socketPort(), name, password.toCharArray());
                socketClient.connect();
            }
            catch (IOException e) {
                dB.echoError("BungeeCord Socket is not online.");
                socketClient.attemptReconnect();
            }
            catch (Exception e) {
                dB.echoError("BungeeCord Socket failed to connect due to an exception.");
                dB.echoError(e);
            }
        }
    }

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    public static boolean isSocketConnected() {
        return socketClient != null && socketClient.isConnected();
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
