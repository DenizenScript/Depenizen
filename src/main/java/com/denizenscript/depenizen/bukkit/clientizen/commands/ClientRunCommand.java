package com.denizenscript.depenizen.bukkit.clientizen.commands;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.clientizen.ClientizenBridge;
import com.denizenscript.depenizen.bukkit.clientizen.network.Channels;
import com.denizenscript.depenizen.bukkit.clientizen.network.DataSerializer;
import com.denizenscript.depenizen.bukkit.clientizen.network.NetworkManager;

import java.util.HashMap;
import java.util.Map;

public class ClientRunCommand extends AbstractCommand {

    public ClientRunCommand() {
        setName("clientrun");
        setSyntax("clientrun [<script>] (path:<name>) (def.<name>:<value>) (defmap:<map>)");
        setRequiredArguments(1, -1);
        allowedDynamicPrefixes = true;
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        PlayerTag player = Utilities.getEntryPlayer(scriptEntry);
        if (player == null || !ClientizenBridge.clientizenPlayers.contains(player.getUUID())) {
            throw new InvalidArgumentsException("No valid clientizen player found.");
        }
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
        String path = (String) scriptEntry.getObject("path");
        ElementTag script = scriptEntry.getElement("script");
        MapTag definitions = scriptEntry.getObjectTag("def_map");
        PlayerTag clientizenPlayer = Utilities.getEntryPlayer(scriptEntry);
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), script, clientizenPlayer, path, definitions);
        }
        Map<String, String> stringDefMap = Map.of();
        if (definitions != null) {
            stringDefMap = new HashMap<>(definitions.map.size());
            for (Map.Entry<StringHolder, ObjectTag> entry : definitions.map.entrySet()) {
                stringDefMap.put(entry.getKey().str, entry.getValue().savable());
            }
        }
        DataSerializer runData = new DataSerializer()
                .writeString(script.asString())
                .writeNullable(path, DataSerializer::writeString)
                .writeStringMap(stringDefMap);
        NetworkManager.send(Channels.RUN_CLIENT_SCRIPT, clientizenPlayer.getPlayerEntity(), runData);
    }
}
