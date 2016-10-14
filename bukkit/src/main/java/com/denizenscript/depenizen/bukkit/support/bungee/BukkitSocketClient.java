package com.denizenscript.depenizen.bukkit.support.bungee;

import com.denizenscript.depenizen.bukkit.Settings;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeTagCommand;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeRegisteredScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerConnectScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerDisconnectScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.ReconnectFailScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.tags.BukkitTagContext;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.ScriptRegistry;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BukkitSocketClient extends SocketClient {

    public BukkitSocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        super(ipAddress, port, name, password);
    }

    @Override
    protected boolean isBungeeScriptCompatible() {
        return true;
    }

    @Override
    protected long getPingDelay() {
        return Settings.socketPingDelay().getMillis();
    }

    @Override
    protected long getPingTimeout() {
        return Settings.socketPingTimeout().getMillis();
    }

    @Override
    protected int getReconnectAttempts() {
        return Settings.socketReconnectAttempts();
    }

    @Override
    protected long getReconnectDelay() {
        return Settings.socketReconnectDelay().getMillis();
    }

    @Override
    protected Set<String> getSubscribedEvents() {
        return BungeeScriptEvent.getInitializedEvents();
    }

    @Override
    protected void fireReconnectFailEvent() {
        ReconnectFailScriptEvent.instance.fire();
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
        BungeeRegisteredScriptEvent.instance.fire();
    }

    @Override
    public void handleUpdateServer(String serverName, boolean registered) {
        if (registered) {
            if (dB.verbose) {
                dB.log("[Bungee]: Registered with " + serverName);
            }
            dServer.addOnlineServer(serverName);
            BungeeServerConnectScriptEvent event = BungeeServerConnectScriptEvent.instance;
            event.server = dServer.getServerFromName(serverName);
            event.fire();
        }
        else {
            if (dB.verbose) {
                dB.log("[Bungee]: Disconnected from " + serverName);
            }
            BungeeServerDisconnectScriptEvent event = BungeeServerDisconnectScriptEvent.instance;
            event.server = dServer.getServerFromName(serverName);
            event.fire();
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
        ScriptQueue queue = InstantQueue.getQueue(ScriptQueue.getNextId("BUNGEE_CMD")).addEntries(scriptEntryList);
        queue.getAllDefinitions().putAll(definitions);
        queue.start();
    }

    @Override
    protected void handleRunScript(String scriptName, Map<String, String> definitions, boolean fullDebug, boolean minimalDebug) {
        if (!ScriptRegistry.containsScript(scriptName)) {
            if (fullDebug) {
                dB.echoError("[BungeeRun]: The script '" + scriptName + "' does not exist!");
            }
            return;
        }
        ScriptContainer scriptContainer = ScriptRegistry.getScriptContainer(scriptName);
        List<ScriptEntry> scriptEntries = scriptContainer.getBaseEntries(new BukkitScriptEntryData(null, null));
        for (ScriptEntry scriptEntry : scriptEntries) {
            scriptEntry.setScript("").fallbackDebug = fullDebug;
        }
        ScriptQueue queue = InstantQueue.getQueue(ScriptQueue.getNextId(scriptContainer.getName())).addEntries(scriptEntries);
        queue.getAllDefinitions().putAll(definitions);
        queue.start();
    }

    @Override
    protected String handleTag(String tag, boolean fullDebug, boolean minimalDebug, Map<String, String> definitions) {
        BukkitTagContext tagContext = new BukkitTagContext(null, null, false, null, fullDebug, null);
        tagContext.definitionProvider.getAllDefinitions().putAll(definitions);
        return TagManager.tag(tag, tagContext);
    }

    @Override
    protected void handleParsedTag(int id, String result) {
        BungeeTagCommand.returnTag(id, result);
    }

    @Override
    protected Map<String, String> handleEvent(String event, Map<String, String> context) {
        return BungeeScriptEvent.fire(event, context);
    }
}
