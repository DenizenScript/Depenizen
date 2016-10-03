package com.denizenscript.depenizen.bukkit;

public class Settings {

    /*

    # BungeeCord Socket configuration

     */

    private static final String SOCKET_ENABLED = "Socket.Enabled";
    private static final String SOCKET_IP_ADDRESS = "Socket.Ip address";
    private static final String SOCKET_PORT = "Socket.Port";
    private static final String SOCKET_PASSWORD = "Socket.Password";
    private static final String SOCKET_NAME = "Socket.Name";
    private static final String SOCKET_TIMEOUT = "Socket.Timeout";
    private static final String SOCKET_RECONNECT_DELAY = "Socket.Reconnect Delay";

    public static boolean socketEnabled() {
        return Depenizen.getCurrentInstance().getConfig().getBoolean(SOCKET_ENABLED, false);
    }

    public static String socketIpAddress() {
        return Depenizen.getCurrentInstance().getConfig().getString(SOCKET_IP_ADDRESS, null);
    }

    public static int socketPort() {
        return Depenizen.getCurrentInstance().getConfig().getInt(SOCKET_PORT, 25578);
    }

    public static String socketPassword() {
        return Depenizen.getCurrentInstance().getConfig().getString(SOCKET_PASSWORD, null);
    }

    public static String socketName() {
        return Depenizen.getCurrentInstance().getConfig().getString(SOCKET_NAME, null);
    }

    public static int socketTimeout() {
        return Depenizen.getCurrentInstance().getConfig().getInt(SOCKET_TIMEOUT, 0);
    }

    public static long socketReconnectDelay() {
        return Depenizen.getCurrentInstance().getConfig().getLong(SOCKET_RECONNECT_DELAY, 10000);
    }
}
