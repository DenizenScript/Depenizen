package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutRunScript;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.BracedCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.HashMap;
import java.util.Map;

public class BungeeRunCommand extends BracedCommand {

    // <--[command]
    // @Name BungeeRun
    // @Syntax bungeerun [<server>|...] [<script_name>] (def:<name>|<value>|...) (debug:<boolean>)
    // @Plugin DepenizenBukkit, DepenizenBungee
    // @Required 2
    // @Stable stable
    // @Short Runs a script on other servers by name via BungeeCord socket.
    // @Author Morphan1

    // @Description
    // This command allows you to run a named script on the specified servers
    // connected to your BungeeCord socket. You may optionally specify a list
    // of definitions to send for the script to use.
    //
    // By default, the script will run with the same debug mode as the queue
    // that runs this command, but you may also specify true or false to force
    // it one way or the other.

    // @Tags
    // None

    // @Usage
    // Use to send network-wide join messages with a script called JoinMessageScript that takes a definition of the player's name.
    // - bungeerun <bungee.list_servers> JoinMessageScript def:<player.name.display>

    // @Usage
    // Use to keep a player's inventory consistent across 2 creative servers.
    // - define player <player>
    // - define contents <player.inventory.list_contents>
    // - bungeerun creative2 UpdatePlayerInventory def:<player>|<player.inventory.list_contents>

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("servers")
                    && arg.matchesArgumentList(dServer.class)) {
                scriptEntry.addObject("servers", arg.asType(dList.class));
            }

            else if (!scriptEntry.hasObject("definitions")
                    && arg.matchesPrefix("d", "def", "define")) {
                scriptEntry.addObject("definitions", arg.asType(dList.class));
            }

            else if (!scriptEntry.hasObject("debug")
                    && arg.matchesPrefix("debug")
                    && arg.matchesPrimitive(aH.PrimitiveType.Boolean)) {
                scriptEntry.addObject("debug", arg.asElement());
            }

            else if (!scriptEntry.hasObject("script_name")) {
                scriptEntry.addObject("script_name", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("servers")) {
            throw new InvalidArgumentsException("Must specify valid server(s)!");
        }
        else if (!scriptEntry.hasObject("script_name")) {
            throw new InvalidArgumentsException("Must specify a script name!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        dList servers = scriptEntry.getdObject("servers");
        Element scriptName = scriptEntry.getElement("script_name");
        dList definitions = scriptEntry.getdObject("definitions");
        Element debug = scriptEntry.getElement("debug");

        dList serverNames = new dList();
        serverNames.setPrefix("servers");

        for (dServer server : servers.filter(dServer.class)) {
            serverNames.add(server.getName());
        }

        dB.report(scriptEntry, getName(), serverNames.debug() + scriptName.debug()
                + (definitions != null ? definitions.debug() : "")
                + (debug != null ? debug.debug() : ""));

        if (BungeeSupport.isSocketConnected()) {
            Map<String, String> finalDefs = new HashMap<String, String>();
            if (definitions != null) {
                if (definitions.size() % 2 != 0) {
                    throw new CommandExecutionException("Uneven number of elements in definitions list!");
                }
                for (int i = 0; i < definitions.size(); i++) {
                    finalDefs.put(definitions.get(i), definitions.get(++i));
                }
            }
            boolean finalDebug = debug != null ? debug.asBoolean() : scriptEntry.shouldDebug();
            ClientPacketOutRunScript runScript = new ClientPacketOutRunScript(serverNames, scriptName.asString(), finalDefs, finalDebug);
            BungeeSupport.getSocketClient().trySend(runScript);
        }
        else {
            dB.echoError("Server is not connected to a BungeeCord Socket.");
        }
    }
}
