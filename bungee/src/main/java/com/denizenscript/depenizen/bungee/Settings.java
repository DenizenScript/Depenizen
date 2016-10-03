package com.denizenscript.depenizen.bungee;

public class Settings {

    /*

    # Whether Depenizen should display debug in the console

    */

    private static final String DEBUG_ENABLED = "Debug.Enabled";

    public static boolean debugEnabled() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getBoolean(DEBUG_ENABLED, true);
    }

    /*

    # SocketServer configuration

     */

    private static final String SOCKET_ENABLED = "Socket.Enabled";
    private static final String SOCKET_PORT = "Socket.Port";
    private static final String SOCKET_MAX_CLIENTS = "Socket.Max clients";
    private static final String SOCKET_PASSWORD = "Socket.Password";

    public static boolean socketEnabled() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getBoolean(SOCKET_ENABLED, false);
    }

    public static int socketPort() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getInt(SOCKET_PORT, 25578);
    }

    public static int socketMaxClients() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getInt(SOCKET_MAX_CLIENTS, 3);
    }

    public static String socketPassword() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getString(SOCKET_PASSWORD, null);
    }
}
