package com.denizenscript.depenizen.sponge.commands.bungee;

import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutRunScript;
import com.denizenscript.depenizen.sponge.support.bungee.BungeeSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BungeeRunCommand extends AbstractCommand {

    // <--[command]
    // @Name bungeerun
    // @Arguments <server list> <script name> [map of definitions] ['none'/'minimal'/'full']
    // @Short Runs a script on other servers by name via BungeeCord socket.
    // @Updated 2016/10/05
    // @Group Depenizen2Sponge
    // @Plugin Depenizen2Sponge, DepenizenBungee
    // @Minimum 2
    // @Maximum 4
    // @Description
    // This command allows you to run a named script on the specified servers
    // connected to your BungeeCord socket. You may optionally specify a list
    // of definitions to send for the script to use.
    //
    // By default, the script will run with the same debug mode as the queue
    // that runs this command, but you may also specify none, minimal, or full
    // to force it to a specific mode.
    // @Example
    // # Use to send network-wide join messages with a script called JoinMessageScript that takes a definition of the player's name.
    // - bungeerun <bungee.list_servers> JoinMessageScript name:<player.name.display>
    // @Example
    // # Use to keep a player's inventory consistent across 2 creative servers without debug.
    // - bungeerun creative2 UpdatePlayerInventory player:<player>|contents:<player.inventory.list_contents> none
    // -->

    @Override
    public String getName() {
        return "bungeerun";
    }

    @Override
    public String getArguments() {
        return "<server list> <script name> [map of definitions] [debug boolean]";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 4;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        ListTag serverListTag = ListTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        List<String> serverList = new ArrayList<>();
        for (AbstractTagObject tagObject : serverListTag.getInternal()) {
            serverList.add(tagObject.toString());
        }
        TextTag scriptNameTag = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 1));
        String scriptName = scriptNameTag.getInternal();
        Map<String, String> definitionsMap = new HashMap<>();
        boolean fullDebug = queue.shouldShowGood();
        boolean minmalDebug = queue.shouldShowError();
        if (entry.arguments.size() > 2) {
            MapTag mapTag = MapTag.getFor(queue.error, entry.getArgumentObject(queue, 2));
            for (Map.Entry<String, AbstractTagObject> mapEntry : mapTag.getInternal().entrySet()) {
                definitionsMap.put(mapEntry.getKey(), mapEntry.getValue().toString());
            }
            if (entry.arguments.size() > 3) {
                TextTag debugTag = TextTag.getFor(queue.error, entry.getArgumentObject(queue, 3));
                try {
                    DebugMode debugMode = DebugMode.valueOf(CoreUtilities.toUpperCase(debugTag.getInternal()));
                    fullDebug = debugMode.showFull;
                    minmalDebug = debugMode.showMinimal;
                }
                catch (IllegalArgumentException e) {
                    queue.handleError(entry, "Invalid debug mode type: " + debugTag.getInternal());
                    return;
                }
            }
        }
        if (!BungeeSupport.isSocketRegistered()) {
            queue.handleError(entry, "Server is not registered to a BungeeCord Socket.");
            return;
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Running the script " + ColorSet.emphasis + scriptNameTag.getInternal() + ColorSet.good
                    + " on the specified servers with the specified debug mode.");
        }
        BungeeSupport.getSocketClient().trySend(new ClientPacketOutRunScript(serverList, scriptName, definitionsMap, fullDebug, minmalDebug));
    }
}
