package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutSetPriority;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.ArrayList;
import java.util.List;

public class BungeePriorityCommand extends AbstractCommand {

    // <--[command]
    // @Name BungeePriority
    // @Syntax bungeepriority [<server>|...]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, DepenizenBungee
    // @Required 1
    // @Stable unknown
    // @Short Sets the server priority list for the bungee connection handler.
    // @Author BlackCoyote

    // @Description
    // With this command you can change the fallback server priority list in bungee.
    // The priority list is used for when a connecting player has no valid reconnect server defined.
    // Bungee will try to connect the player to first responsive server in the list.

    // @Tags
    // <bungee.list_servers>

    // @Usage
    // Sets the fallback priority to first try the server "lobby", then "survival", and finally "creative".
    // - bungeepriority "server@lobby|server@survival|server@creative"

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("list")) {
                scriptEntry.addObject("list", arg.asType(dList.class).filter(dServer.class));
            }
            else {
                arg.reportUnhandled();
            }

        }

        if (!scriptEntry.hasObject("list")) {
            throw new InvalidArgumentsException("Must specify a list of servers to update the priority list to!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        List<dServer> serverlist = (List<dServer>) scriptEntry.getObject("list");

        dB.report(scriptEntry, getName(), aH.debugList("servers", serverlist));

        List<String> servernames = new ArrayList<String>();
        for (dServer server : serverlist) {
            servernames.add(server.getName());
        }

        BungeeSupport.getSocketClient().trySend(new ClientPacketOutSetPriority(servernames));
    }
}
