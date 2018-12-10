package com.denizenscript.depenizen.bukkit.support.bungee;

import com.denizenscript.depenizen.bukkit.Settings;
import com.denizenscript.depenizen.bukkit.commands.bungee.BungeeTagCommand;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeRegisteredScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerConnectScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.BungeeServerDisconnectScriptEvent;
import com.denizenscript.depenizen.bukkit.events.bungee.ReconnectFailScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.common.socket.DataDeserializer;
import com.denizenscript.depenizen.common.socket.Packet;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import com.denizenscript.depenizen.common.util.SimpleScriptEntry;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.tags.BukkitTagContext;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.ScriptRegistry;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.Bukkit;

import java.security.GeneralSecurityException;
import java.util.*;

public class BukkitSocketClient extends SocketClient {

    public BukkitSocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        super(ipAddress, port, name, password);
    }

    @Override
    protected void receivePacket(final Packet.ClientBound packetType, final DataDeserializer data) {
        Bukkit.getScheduler().runTask(DenizenAPI.getCurrentInstance(), new Runnable() {
            @Override
            public void run() {
                BukkitSocketClient.super.receivePacket(packetType, data);
            }
        });
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

    public Map<String, dObject> objectify(Map<String, String> defs) {
        Map<String, dObject> toret = new HashMap<String, dObject>();
        for (Map.Entry<String, String> def : defs.entrySet()) {
            toret.put(def.getKey(), ObjectFetcher.pickObjectFor(def.getValue()));
        }
        return toret;
    }

    @Override
    protected void handleScript(boolean shouldDebug, List<SimpleScriptEntry> scriptEntries, Map<String, String> definitions) {
        List<ScriptEntry> scriptEntryList = new ArrayList<ScriptEntry>();
        for (SimpleScriptEntry entry : scriptEntries) {
            List<String> args = entry.getArguments();
            ScriptEntry scriptEntry = new ScriptEntry(entry.getCommand(), args.toArray(new String[args.size()]), null);
            scriptEntry.fallbackDebug = shouldDebug;
            scriptEntryList.add(scriptEntry);
        }
        ScriptQueue queue = InstantQueue.getQueue(ScriptQueue.getNextId("BUNGEE_CMD")).addEntries(scriptEntryList);
        queue.getAllDefinitions().putAll(objectify(definitions));
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
        queue.getAllDefinitions().putAll(objectify(definitions));
        queue.start();
    }

    @Override
    protected String handleTag(String tag, boolean fullDebug, boolean minimalDebug, Map<String, String> definitions) {
        BukkitTagContext tagContext = new BukkitTagContext(null, null, false, null, fullDebug, null);
        tagContext.definitionProvider.getAllDefinitions().putAll(objectify(definitions));
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
