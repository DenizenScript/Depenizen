package com.denizenscript.depenizen.bukkit.events.bungee;

import com.denizenscript.depenizen.bukkit.support.bungee.SocketClient;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.bukkit.support.bungee.packets.ClientPacketOutEventSubscribe;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import java.util.HashMap;
import java.util.Map;

public abstract class BungeeScriptEvent extends BukkitScriptEvent {

    private static Map<String, BungeeScriptEvent> initializedEvents = new HashMap<String, BungeeScriptEvent>();

    public static Map<String, String> fire(String event, Map<String, String> context) {
        if (initializedEvents.containsKey(event)) {
            return initializedEvents.get(event).fire(context);
        }
        return null;
    }

    @Override
    public void init() {
        SocketClient socketClient = BungeeSupport.getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            String name = getName();
            initializedEvents.put(name, this);
            socketClient.send(new ClientPacketOutEventSubscribe(ClientPacketOutEventSubscribe.Action.SUBSCRIBE, name));
        }
    }

    @Override
    public void destroy() {
        SocketClient socketClient = BungeeSupport.getSocketClient();
        if (socketClient != null && socketClient.isConnected()) {
            String name = getName();
            initializedEvents.remove(name);
            socketClient.send(new ClientPacketOutEventSubscribe(ClientPacketOutEventSubscribe.Action.UNSUBSCRIBE, name));
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
