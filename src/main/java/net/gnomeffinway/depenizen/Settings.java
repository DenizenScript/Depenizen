package net.gnomeffinway.depenizen;

public class Settings {

    /*

    # BungeeCord Socket configuration

     */

    private static final String SOCKET_ENABLED = "Socket.Enabled";
    private static final String SOCKET_IP_ADDRESS = "Socket.Ip address";
    private static final String SOCKET_PORT = "Socket.Port";
    private static final String SOCKET_PASSWORD = "Socket.Password";

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
}
