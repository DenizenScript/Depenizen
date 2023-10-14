package com.denizenscript.depenizen.bukkit.clientizen.commands;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import com.denizenscript.depenizen.bukkit.clientizen.network.NetworkManager;
import com.denizenscript.depenizen.bukkit.clientizen.network.packets.RunClientScriptPacketOut;

import java.util.HashMap;
import java.util.Map;

public class ClientRunCommand extends AbstractCommand {

    // <--[command]
    // @Name ClientRun
    // @Syntax clientrun [<script>] (path:<name>) (def.<name>:<value>) (defmap:<map>)
    // @Required 1
    // @Maximum -1
    // @Short Runs a client script on a Clientizen client.
    // @Group Depenizen
    // @plugin Depenizen, Clientizen
    //
    // @Description
    // Runs a client script on the linked player's client, if they are using Clientizen.
    //
    // You must specify a client script name to run.
    //
    // Optionally, use the "path:" argument to choose a specific sub-path within a script.
    //
    // Optionally, use "def.<name>:<value>" to pass one or more definitions to the client.
    // Alternately, use "defmap:<map>" to specify definitions to pass as a MapTag, where the keys will be definition names and the values will of course be definition values.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run a task script named 'MyTask' on the linked player's client.
    // - clientrun MyTask
    //
    // @Usage
    // Use to run the sub-script under 'sub_path' in a task script named 'MyTask' on the linked player's client.
    // - clientrun MyTask path:sub_path
    //
    // @Usage
    // Use to run 'MyTask' on the linked player's client and pass 2 definitions to it.
    // - clientrun MyTask def.amount:42 def.location:<server.flag[spawn_location]>
    //
    // @Usage
    // Use to run 'MyTask' on a specific Clientizen player's client.
    // - clientrun MyTask player:<[clientizenPlayer]>
    //
    // -->

    public ClientRunCommand() {
        setName("clientrun");
        setSyntax("clientrun [<script>] (path:<name>) (def.<name>:<value>) (defmap:<map>)");
        setRequiredArguments(1, -1);
        allowedDynamicPrefixes = true;
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        MapTag defMap = new MapTag();
        for (Argument arg : scriptEntry) {
            if (arg.matchesPrefix("defmap")
                    && arg.matchesArgumentType(MapTag.class)) {
                defMap.map.putAll(arg.asType(MapTag.class).map);
            }
            else if (arg.hasPrefix()
                    && arg.getPrefix().getRawValue().startsWith("def.")) {
                defMap.putObject(arg.getPrefix().getRawValue().substring("def.".length()), arg.object);
            }
            else if (!arg.hasPrefix() && arg.getRawValue().startsWith("def.") && arg.getRawValue().contains(":")) {
                String rawValue = arg.getRawValue();
                int colon = rawValue.indexOf(':');
                defMap.putObject(rawValue.substring("def.".length(), colon), new ElementTag(rawValue.substring(colon + 1)));
            }
            else if (!scriptEntry.hasObject("path")
                    && arg.matchesPrefix("path")) {
                scriptEntry.addObject("path", arg.asElement().asString());
            }
            else if (!scriptEntry.hasObject("script")) {
                scriptEntry.addObject("script", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("script")) {
            throw new InvalidArgumentsException("Must specify a script to run on the client.");
        }
        if (!defMap.map.isEmpty()) {
            scriptEntry.addObject("def_map", defMap);
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        PlayerTag clientizenPlayer = Utilities.getEntryPlayer(scriptEntry);
        if (clientizenPlayer == null) {
            throw new InvalidArgumentsRuntimeException("Must have a linked player who's client the script will be ran on, but none was found.");
        }
        if (!ClientizenBridge.clientizenPlayers.contains(clientizenPlayer.getUUID())) {
            throw new InvalidArgumentsRuntimeException("Player found, but isn't running Clientizen.");
        }
        String path = (String) scriptEntry.getObject("path");
        ElementTag script = scriptEntry.getElement("script");
        MapTag definitions = scriptEntry.getObjectTag("def_map");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), script, clientizenPlayer, path, definitions);
        }
        Map<String, String> stringDefMap = Map.of();
        if (definitions != null) {
            stringDefMap = new HashMap<>(definitions.size());
            for (Map.Entry<StringHolder, ObjectTag> entry : definitions.entrySet()) {
                stringDefMap.put(entry.getKey().str, entry.getValue().savable());
            }
        }
        NetworkManager.send(clientizenPlayer.getPlayerEntity(), new RunClientScriptPacketOut(script.asString(), path, stringDefMap));
    }
}
