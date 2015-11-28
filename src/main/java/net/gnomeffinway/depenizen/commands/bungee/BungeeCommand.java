package net.gnomeffinway.depenizen.commands.bungee;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.BracedCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.bungee.BungeeSupport;
import net.gnomeffinway.depenizen.support.bungee.packets.ClientPacketOutScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BungeeCommand extends BracedCommand {

    // <--[command]
    // @Name Bungee
    // @Syntax bungee [<server>|...] [<commands>]
    // @Group Depenizen
    // @Plugin Depenizen, BungeeCord
    // @Required 2
    // @Stable stable
    // @Short Sends commands to other servers via BungeeCord.
    // @Author Morphan1

    // @Description
    // This command allows you to send multiple lines of script to your BungeeCord
    // server, which then relays it to the dServer you specified. Tags and definitions
    // will be parsed on the dServer, and the definitions in the current queue will
    // carry over.

    // @Tags
    // None

    // @Usage
    // Use to send network-wide join messages.
    // - define name <player.name.display>
    // - bungee <bungee.list_servers> {
    //   - announce "<yellow>%name% has joined the network."
    //   }

    // @Usage
    // Use to keep a player's inventory consistent across 2 creative servers.
    // - define player <player>
    // - define contents <player.inventory.list_contents>
    // - bungee creative2 {
    //   - if <def[player].has_played_before||false> {
    //     - inventory set d:<def[player].inventory> o:<def[contents]>
    //     }
    //   }

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("servers")
                    && !scriptEntry.hasObject("all-servers")
                    && arg.matchesArgumentList(dServer.class)) {
                scriptEntry.addObject("servers", arg.asType(dList.class));
            }

            else if (!scriptEntry.hasObject("servers")
                    && !scriptEntry.hasObject("all-servers")
                    && arg.matches("all")) {
                scriptEntry.addObject("all-servers", new Element(true));
            }

            else if (arg.matches("{")) {
                scriptEntry.addObject("braces", getBracedCommands(scriptEntry));
                break;
            }

        }

        if (!scriptEntry.hasObject("servers") && !scriptEntry.hasObject("all-servers"))
            throw new InvalidArgumentsException("Must specify valid server(s)!");

        if (!scriptEntry.hasObject("braces"))
            throw new InvalidArgumentsException("Must have braces!");

        scriptEntry.defaultObject("all-servers", new Element(false));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element all = scriptEntry.getElement("all-servers");
        dList servers = scriptEntry.getdObject("servers");
        List<ScriptEntry> bracedCommands = ((List<BracedData>) scriptEntry.getObject("braces")).get(0).value;

        dList serverNames = new dList();
        serverNames.setPrefix("servers");

        if (all != null && all.asBoolean()) {
            dB.echoError("Argument 'ALL' is deprecated and will be removed in the future. Please use <bungee.list_servers> instead!");
            for (dServer server : dServer.getOnlineServers().values()) {
                serverNames.add(server.getName());
            }
        }
        else {
            for (dServer server : servers.filter(dServer.class)) {
                serverNames.add(server.getName());
            }
        }

        dB.report(scriptEntry, getName(), serverNames.debug());

        if (BungeeSupport.isSocketConnected()) {
            boolean debug = scriptEntry.shouldDebug();
            Map<String, String> definitions = scriptEntry.getResidingQueue().getAllDefinitions();
            ClientPacketOutScript packet = new ClientPacketOutScript(serverNames, debug, bracedCommands, definitions);
            BungeeSupport.getSocketClient().send(packet);
        }
        else {
            dB.echoError("Server is not connected to a BungeeCord Socket.");
        }
    }
}
