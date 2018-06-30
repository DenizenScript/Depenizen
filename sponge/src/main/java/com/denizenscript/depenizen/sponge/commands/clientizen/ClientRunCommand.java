package com.denizenscript.depenizen.sponge.commands.clientizen;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2sponge.tags.objects.PlayerTag;

import java.util.HashMap;
import java.util.Map;

public class ClientRunCommand extends AbstractCommand {

    // <--[command]
    // @Name clientrun
    // @Arguments <player> <script name> [map of definitions]
    // @Short Runs a client script on a player's client, assuming they have the Forge mod 'Clientizen'.
    // @Updated 2016/11/11
    // @Group Depenizen2Sponge
    // @Plugin Depenizen2Sponge
    // @Minimum 2
    // @Maximum 3
    // @Description
    // This command allows you to run a named script on the client. You may
    // optionally specify a map of definitions to send for the script to use.
    // @Example
    // Runs the script 'server_update_gui' on the client.
    // - clientrun server_update_gui text:Hello
    // -->

    @Override
    public String getName() {
        return "clientrun";
    }

    @Override
    public String getArguments() {
        return "<player> <script name> [map of definitions]";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 3;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        PlayerTag playerTag = PlayerTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        TextTag scriptNameTag = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 1));
        String scriptName = scriptNameTag.getInternal();
        Map<String, String> definitionsMap = new HashMap<>();
        if (entry.arguments.size() > 2) {
            MapTag mapTag = MapTag.getFor(queue.error, entry.getArgumentObject(queue, 2));
            for (Map.Entry<String, AbstractTagObject> mapEntry : mapTag.getInternal().entrySet()) {
                definitionsMap.put(mapEntry.getKey(), mapEntry.getValue().toString());
            }
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Running the script " + ColorSet.emphasis + scriptNameTag.getInternal() + ColorSet.good
                    + " on the linked player's client!");
        }
        // TODO: Implement!
    }
}
