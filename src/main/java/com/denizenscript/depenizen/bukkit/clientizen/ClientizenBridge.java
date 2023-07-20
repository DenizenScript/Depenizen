package com.denizenscript.depenizen.bukkit.clientizen;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.events.bukkit.ScriptReloadEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptHelper;
import com.denizenscript.denizencore.utilities.CoreConfiguration;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.clientizen.commands.ClientRunCommand;
import com.denizenscript.depenizen.bukkit.clientizen.network.NetworkManager;
import com.denizenscript.depenizen.bukkit.clientizen.network.packets.FireEventPacketIn;
import com.denizenscript.depenizen.bukkit.clientizen.network.packets.ReceiveConfirmPacketIn;
import com.denizenscript.depenizen.bukkit.clientizen.network.packets.SetScriptsPacketOut;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ClientizenBridge implements Listener {

    public static Map<String, String> clientizenScripts = new HashMap<>();
    public static SetScriptsPacketOut scriptsPacket;
    public static Set<UUID> clientizenPlayers = new HashSet<>();

    public static File clientizenFolder = new File(Denizen.instance.getDataFolder(), "client-scripts");

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new ClientizenBridge(), Depenizen.instance);
        // Setup client scripts folder
        clientizenFolder.mkdir();
        // Networking
        NetworkManager.init();
        NetworkManager.registerInPacket(new ReceiveConfirmPacketIn());
        // Scripts features
        ScriptEvent.registerScriptEvent(ClientizenEventScriptEvent.class);
        NetworkManager.registerInPacket(new FireEventPacketIn());
        DenizenCore.commandRegistry.registerCommand(ClientRunCommand.class);

        // <--[tag]
        // @attribute <PlayerTag.has_clientizen>
        // @returns ElementTag(Boolean)
        // @group Clientizen
        // @description
        // Returns whether the player has Clientizen running on their client.
        // -->
        PlayerTag.registerOnlineOnlyTag(ElementTag.class, "has_clientizen", (attribute, object) -> {
            return new ElementTag(clientizenPlayers.contains(object.getUUID()));
        });
        Debug.log("Clientizen support enabled!");
    }

    public static void acceptNewPlayer(Player player) {
        clientizenPlayers.add(player.getUniqueId());
        NetworkManager.send(player, scriptsPacket);
    }

    // TODO: load client scripts async, same as regular ones
    public static void reloadClientScripts() {
        clientizenScripts.clear();
        for (File file : CoreUtilities.listDScriptFiles(clientizenFolder)) {
            String name = CoreUtilities.toLowerCase(file.getName());
            if (clientizenScripts.containsKey(name)) {
                Debug.echoError("Multiple script files named '" + name + "' found in client-scripts folder!");
                continue;
            }
            try (FileInputStream stream = new FileInputStream(file)) {
                // TODO: clear comments server-side
                clientizenScripts.put(name, ScriptHelper.convertStreamToString(stream));
                if (CoreConfiguration.debugLoadingInfo) {
                    Debug.log("Loaded client script: " + name);
                }
            }
            catch (Exception e) {
                Debug.echoError("Failed to load client script file '" + name + "', see below stack trace:");
                Debug.echoError(e);
            }
        }
        scriptsPacket = new SetScriptsPacketOut(clientizenScripts);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clientizenPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onScriptsReload(ScriptReloadEvent event) {
        reloadClientScripts();
        NetworkManager.broadcast(scriptsPacket);
    }
}
