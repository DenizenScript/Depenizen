package com.denizenscript.depenizen.sponge.support.bungee;

import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.depenizen.common.socket.client.SocketClient;
import com.denizenscript.depenizen.sponge.Settings;
import com.denizenscript.depenizen.sponge.tags.bungee.objects.BungeeServerTag;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpongeSocketClient extends SocketClient {

    public SpongeSocketClient(String ipAddress, int port, String name, char[] password) throws GeneralSecurityException {
        super(ipAddress, port, name, password);
    }

    @Override
    protected boolean isBungeeScriptCompatible() {
        return false;
    }

    @Override
    protected long getPingDelay() {
        return (long) Settings.socketPingDelay().seconds() * 1000;
    }

    @Override
    protected long getPingTimeout() {
        return (long) Settings.socketPingDelay().seconds() * 1000;
    }

    @Override
    protected int getReconnectAttempts() {
        return Settings.socketReconnectAttempts();
    }

    @Override
    protected long getReconnectDelay() {
        return (long) Settings.socketPingDelay().seconds() * 1000;
    }

    @Override
    protected Set<String> getSubscribedEvents() {
        return new HashSet<>();
    }

    @Override
    protected void fireReconnectFailEvent() {
        // TODO: event
    }

    @Override
    public void handleAcceptRegister(String registrationName, List<String> existingServers) {
        BungeeServerTag.addOnlineServer(registrationName);
        for (String server : existingServers) {
            if (!server.isEmpty()) {
                BungeeServerTag.addOnlineServer(server);
            }
        }
    }

    @Override
    public void handleUpdateServer(String serverName, boolean registered) {
        // TODO: events
        if (registered) {
            BungeeServerTag.addOnlineServer(serverName);
            //BungeeServerConnectScriptEvent event = BungeeServerConnectScriptEvent.instance;
            //event.server = dServer.getServerFromName(serverName);
            //event.fire();
        }
        else {
            //BungeeServerDisconnectScriptEvent event = BungeeServerDisconnectScriptEvent.instance;
            //event.server = dServer.getServerFromName(serverName);
            //event.fire();
            BungeeServerTag.removeOnlineServer(serverName);
        }
    }

    @Override
    protected void handleScript(boolean shouldDebug, Map<String, List<String>> scriptEntries, Map<String, String> definitions) {
        // DepenizenBukkit only
    }

    @Override
    protected void handleRunScript(String scriptName, Map<String, String> definitions, boolean fullDebug, boolean minimalDebug) {
        CommandScript script = Denizen2Core.currentScripts.get(CoreUtilities.toLowerCase(scriptName));
        if (script == null) {
            if (minimalDebug) {
                Denizen2Core.getImplementation().outputError("The script '" + scriptName + "' does not exist!");
            }
            return;
        }
        if (!(script instanceof TaskScript)) {
            if (minimalDebug) {
                Denizen2Core.getImplementation().outputError("Trying to run a non-task typed script!");
            }
            return;
        }
        TaskScript task = (TaskScript) script;
        CommandScriptSection section = task.getSection("script");
        if (section == null) {
            if (minimalDebug) {
                Denizen2Core.getImplementation().outputError("Invalid script section!");
            }
            return;
        }
        if (fullDebug) {
            Denizen2Core.getImplementation().outputGood("Running script: " + ColorSet.emphasis + script.title);
        }
        CommandQueue nq = section.toQueue();
        if (definitions.size() > 1) {
            Map<String, AbstractTagObject> defs = new HashMap<>();
            for (Map.Entry<String, String> entry : definitions.entrySet()) {
                defs.put(entry.getKey(), new TextTag(entry.getValue())); // TODO: proper objects?
            }
            nq.commandStack.peek().definitions.putAll(defs);
        }
        DebugMode debugMode = minimalDebug ? fullDebug ? DebugMode.FULL : DebugMode.MINIMAL : DebugMode.NONE;
        nq.commandStack.peek().setDebugMode(debugMode);
        nq.start();
    }

    @Override
    protected String handleTag(String tag, boolean fullDebug, boolean minimalDebug, Map<String, String> definitions) {
        // TODO: command for this
        HashMap<String, AbstractTagObject> defs = new HashMap<>();
        for (Map.Entry<String, String> entry : definitions.entrySet()) {
            defs.put(entry.getKey(), new TextTag(entry.getValue())); // TODO: proper objects?
        }
        Action<String> error = (e) -> Denizen2Core.getImplementation().outputError(e);
        DebugMode debugMode = minimalDebug ? fullDebug ? DebugMode.FULL : DebugMode.MINIMAL : DebugMode.NONE;
        return Denizen2Core.splitToArgument(tag, false, false, error).parse(null, defs, debugMode, error).toString();
    }

    @Override
    protected void handleParsedTag(int id, String result) {
        // TODO: BungeeTagCommand.returnTag(id, result);
    }

    @Override
    protected Map<String, String> handleEvent(String event, Map<String, String> context) {
        //return BungeeScriptEvent.fire(event, context);
        return new HashMap<>();
    }
}
