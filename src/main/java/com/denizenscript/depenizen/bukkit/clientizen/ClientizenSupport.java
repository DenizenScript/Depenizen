package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.events.bukkit.ScriptReloadEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptHelper;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.clientizen.events.ClientizenEventManager;
import com.denizenscript.depenizen.bukkit.clientizen.events.ClientizenEventRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ClientizenSupport implements Listener {

    public static ClientizenSupport instance;

    public static final Map<String, String> clientizenScripts = new HashMap<>();
    public static DataSerializer scriptsSerializer;
    public static final Set<UUID> clientizenPlayers = new HashSet<>();

    public static File clientizenFolder;

    public static void init() {
        instance = new ClientizenSupport();
        clientizenFolder = new File(Denizen.instance.getDataFolder(), "client-scripts");
        clientizenFolder.mkdir();
        Bukkit.getPluginManager().registerEvents(instance, Depenizen.instance);
        // A tag for testing
        PlayerTag.registerOnlineOnlyTag(ElementTag.class, "is_clientizen", (attribute, object) -> {
            return new ElementTag(clientizenPlayers.contains(object.getUUID()));
        });
        NetworkManager.init();
        NetworkManager.registerInChannel(Channels.RECEIVE_CONFIRM, (player, message) -> {
            Debug.log("Received confirmation from " + player.getName());
            clientizenPlayers.add(player.getUniqueId());
            // Wait a little to make sure the client is ready to receive packets
            Bukkit.getScheduler().runTaskLater(Depenizen.instance, () -> acceptNewPlayer(player), 20);
        });
        ClientizenEventManager.init();
        ClientizenEventRegistry.registerEvents();
        Debug.log("Clientizen support enabled!");
    }

    public static void reloadClientScripts() {
        clientizenScripts.clear();
        List<File> scriptFiles = CoreUtilities.listDScriptFiles(clientizenFolder);
        for (File file : scriptFiles) {
            String name = CoreUtilities.toLowerCase(file.getName());
            if (clientizenScripts.containsKey(name)) {
                Debug.echoError("Multiple script files named '" + name + "' found in client-scripts folder!");
                continue;
            }
            try (FileInputStream stream = new FileInputStream(file)) {
                // TODO: clear comments server-side
                clientizenScripts.put(name, ScriptHelper.convertStreamToString(stream));
                Debug.log("Loaded client script: " + name);
            }
            catch (Exception e) {
                Debug.echoError("Failed to load client script file '" + name + "', see below stack trace:");
                Debug.echoError(e);
            }
        }
        scriptsSerializer = new DataSerializer().writeStringMap(clientizenScripts);
    }

    public static void resendClientScripts() {
        NetworkManager.broadcast(Channels.SET_SCRIPTS, scriptsSerializer);
    }

    public static void acceptNewPlayer(Player player) {
        NetworkManager.send(Channels.SET_SCRIPTS, player, scriptsSerializer);
        NetworkManager.send(Channels.EVENT_DATA, player, ClientizenEventManager.eventsSerializer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clientizenPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onScriptsReload(ScriptReloadEvent event) {
        reloadClientScripts();
        resendClientScripts();
        ClientizenEventManager.reload();
    }
}
