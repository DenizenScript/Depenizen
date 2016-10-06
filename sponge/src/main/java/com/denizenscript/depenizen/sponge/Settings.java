package com.denizenscript.depenizen.sponge;

import com.denizenscript.denizen2core.tags.objects.DurationTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

public class Settings {

    private static void handleConfigError(String node, String error) {
        Depenizen2Sponge.instance.debugError("'" + node + "' in Depenizen2Sponge config.yml has an error: " + error);
    }

    /*

    # BungeeCord Socket configuration

     */

    public static boolean socketEnabled() {
        return CoreUtilities.toLowerCase(Depenizen2Sponge.instance.config.getString("Socket.Enabled", "false")).equals("true");
    }

    public static String socketIpAddress() {
        return Depenizen2Sponge.instance.config.getString("Socket.IP Address", null);
    }

    public static int socketPort() {
        return Integer.valueOf(Depenizen2Sponge.instance.config.getString("Socket.Port", "25578"));
    }

    public static String socketPassword() {
        return Depenizen2Sponge.instance.config.getString("Socket.Password", null);
    }

    public static String socketName() {
        return Depenizen2Sponge.instance.config.getString("Socket.Name", null);
    }

    public static DurationTag socketPingDelay() {
        return DurationTag.getFor((error) -> handleConfigError("Socket.Ping Delay", error),
                Depenizen2Sponge.instance.config.getString("Socket.Ping Delay", "30s"));
    }

    public static DurationTag socketPingTimeout() {
        return DurationTag.getFor((error) -> handleConfigError("Socket.Ping Timeout", error),
                Depenizen2Sponge.instance.config.getString("Socket.Ping Timeout", "30s"));
    }

    public static DurationTag socketReconnectDelay() {
        return DurationTag.getFor((error) -> handleConfigError("Socket.Reconnection Delay", error),
                Depenizen2Sponge.instance.config.getString("Socket.Reconnection Delay", "10s"));
    }

    public static int socketReconnectAttempts() {
        return Integer.valueOf(Depenizen2Sponge.instance.config.getString("Socket.Reconnection Attempts", "10"));
    }
}
