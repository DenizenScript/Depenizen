package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutEventSubscription;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BungeeScriptEvent extends BukkitScriptEvent {

    private static Map<String, BungeeScriptEvent> initializedEvents = new HashMap<String, BungeeScriptEvent>();

    public static Map<String, String> fire(String event, Map<String, String> context) {
        if (initializedEvents.containsKey(event)) {
            return initializedEvents.get(event).fire(context);
        }
        return null;
    }

    public static Set<String> getInitializedEvents() {
        return initializedEvents.keySet();
    }

    @Override
    public void init() {
        String name = getName();
        initializedEvents.put(name, this);
        SocketClient socketClient = BungeeSupport.getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            socketClient.trySend(new ClientPacketOutEventSubscription(name, true));
        }
    }

    @Override
    public void destroy() {
        String name = getName();
        initializedEvents.remove(name);
        SocketClient socketClient = BungeeSupport.getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            socketClient.trySend(new ClientPacketOutEventSubscription(name, false));
        }
    }

    public abstract Map<String, String> fire(Map<String, String> context);

    public boolean tryServer(dServer server, String comparedto) {
        comparedto = CoreUtilities.toLowerCase(comparedto);
        if (comparedto.equals("server")) {
            return true;
        }
        if (comparedto.length() == 0) {
            dB.echoError("tryServer missing server value when compared to " + server.getName());
            return false;
        }
        return CoreUtilities.toLowerCase(server.getName()).equals(comparedto);
    }
}
