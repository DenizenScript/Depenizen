package com.denizenscript.depenizen.bukkit.support.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BukkitSocketClient extends SocketClient {

    public BukkitSocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        super(ipAddress, port, name, password);
    }

    @Override
    protected boolean isBungeeScriptCompatible() {
        return true;
    }

    @Override
    public void handleAcceptRegister(String registrationName, List<String> existingServers) {
        dServer.addOnlineServer(registrationName);
        for (String server : existingServers) {
            if (!server.isEmpty()) {
                if (dB.verbose) {
                    dB.log("[Bungee]: Registered with " + server);
                }
                dServer.addOnlineServer(server);
            }
        }
    }

    @Override
    public void handleUpdateServer(String serverName, boolean registered) {
        if (registered) {
            if (dB.verbose) {
                dB.log("[Bungee]: Registered with " + serverName);
            }
            dServer.addOnlineServer(serverName);
        }
        else {
            if (dB.verbose) {
                dB.log("[Bungee]: Disconnected from " + serverName);
            }
            dServer.removeOnlineServer(serverName);
        }
    }

    @Override
    protected void handleScript(boolean shouldDebug, Map<String, List<String>> scriptEntries, Map<String, String> definitions) {
        List<ScriptEntry> scriptEntryList = new ArrayList<ScriptEntry>();
        try {
            for (Map.Entry<String, List<String>> entry : scriptEntries.entrySet()) {
                List<String> value = entry.getValue();
                ScriptEntry scriptEntry = new ScriptEntry(entry.getKey(), value.toArray(new String[value.size()]), null);
                scriptEntry.fallbackDebug = shouldDebug;
                scriptEntryList.add(scriptEntry);
            }
        }
        catch (ScriptEntryCreationException e) {
            dB.echoError(e);
            return;
        }
        InstantQueue queue = new InstantQueue(ScriptQueue.getNextId("BUNGEE_CMD"));
        queue.addEntries(scriptEntryList);
        queue.getAllDefinitions().putAll(definitions);
        queue.start();
    }
}
