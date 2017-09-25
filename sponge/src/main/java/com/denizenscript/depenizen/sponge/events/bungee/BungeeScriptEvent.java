package com.denizenscript.depenizen.sponge.events.bungee;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutEventSubscription;
import com.denizenscript.depenizen.sponge.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BungeeScriptEvent extends ScriptEvent {

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
    public void enable() {
        String name = getName();
        initializedEvents.put(name, this);
        if (BungeeSupport.isSocketRegistered()) {
            BungeeSupport.getSocketClient().trySend(new ClientPacketOutEventSubscription(name, true));
        }
    }

    @Override
    public void disable() {
        String name = getName();
        initializedEvents.remove(name);
        if (BungeeSupport.isSocketRegistered()) {
            BungeeSupport.getSocketClient().trySend(new ClientPacketOutEventSubscription(name, false));
        }
    }

    public abstract Map<String, String> fire(Map<String, String> context);

    public static boolean checkServer(String server, ScriptEventData data, Action<String> error) {
        return checkServer(server, data, error, "server");
    }

    public static boolean checkServer(String server, ScriptEventData data, Action<String> error, String key) {
        if (!data.switches.containsKey(key)) {
            return true;
        }
        for (AbstractTagObject ato : ListTag.getFor(error, data.switches.get(key)).getInternal()) {
            if ((BungeeServerTag.getFor(error, ato)).getInternal().equals(server)) {
                return true;
            }
        }
        return false;
    }
}
