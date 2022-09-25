package com.denizenscript.depenizen.bukkit.clientizen.events;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.clientizen.Channels;
import com.denizenscript.depenizen.bukkit.clientizen.DataSerializer;
import com.denizenscript.depenizen.bukkit.clientizen.NetworkManager;

import java.util.HashMap;
import java.util.Map;

public class ClientizenEventManager {

    private static final Map<String, ClientizenScriptEvent> clientizenEvents = new HashMap<>();
    public static DataSerializer eventsSerializer;

    public static void init() {
        NetworkManager.registerInChannel(Channels.RECEIVE_EVENT, (player, message) -> {
            String id = message.readString();
            ClientizenScriptEvent event = clientizenEvents.get(id);
            if (event == null) {
                Debug.echoError("Received invalid event '" + id + "' from '" + player.getName() + "'.");
                return;
            }
            event.fireInternal(player, message);
        });
        ScriptEvent.notNameParts.add("ClientizenEvent");
    }

    public static void registerEvent(Class<? extends ClientizenScriptEvent> eventClass) {
        try {
            ClientizenScriptEvent instance = eventClass.getConstructor().newInstance();
            if (clientizenEvents.containsKey(instance.id)) {
                Debug.echoError("Tried registering event '" + instance.id + "' but an event with that ID is already registered.");
                return;
            }
            ScriptEvent.registerScriptEvent(instance);
            clientizenEvents.put(instance.id, instance);
        }
        catch (Exception ex) {
            Debug.echoError("Something went wrong while registering clientizen event '" + eventClass.getName() + "':");
            Debug.echoError(ex);
        }
    }

    public static void reload() {
        reloadEvents();
        NetworkManager.broadcast(Channels.EVENT_DATA, eventsSerializer);
    }

    private static void reloadEvents() {
        int size = 0;
        DataSerializer eventData = new DataSerializer();
        for (Map.Entry<String, ClientizenScriptEvent> entry : clientizenEvents.entrySet()) {
            ClientizenScriptEvent event = entry.getValue();
            if (event.enabled) {
                eventData.writeString(entry.getKey());
                event.write(eventData);
                size++;
            }
        }
        eventsSerializer = new DataSerializer().writeInt(size).writeBytes(eventData.toByteArray());
    }
}
