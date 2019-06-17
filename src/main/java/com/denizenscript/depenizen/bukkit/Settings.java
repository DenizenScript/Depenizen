package com.denizenscript.depenizen.bukkit;

import net.aufdemrand.denizencore.objects.Duration;

public class Settings {

    /*

    # BungeeCord Socket configuration

     */

    public static boolean socketEnabled() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getBoolean("Socket.Enabled", false);
    }

    public static String socketIpAddress() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.IP Address", null);
    }

    public static int socketPort() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getInt("Socket.Port", 25578);
    }

    public static String socketPassword() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.Password", null);
    }

    public static String socketName() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.Name", null);
    }

    public static Duration socketPingDelay() {
        return Duration.valueOf(DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.Ping Delay", "30s"));
    }

    public static Duration socketPingTimeout() {
        return Duration.valueOf(DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.Ping Timeout", "30s"));
    }

    public static Duration socketReconnectDelay() {
        if (DepenizenPlugin.getCurrentInstance().getConfig().contains("Socket.Reconnection Delay")) {
            return Duration.valueOf(DepenizenPlugin.getCurrentInstance().getConfig().getString("Socket.Reconnection Delay", "10s"));
        }
        // For backwards compatibility
        return new Duration((int) DepenizenPlugin.getCurrentInstance().getConfig().getLong("Socket.Reconnect Delay", 10000) / 1000);
    }

    public static int socketReconnectAttempts() {
        return DepenizenPlugin.getCurrentInstance().getConfig().getInt("Socket.Reconnection Attempts", 10);
    }
}
