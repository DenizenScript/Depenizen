package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutExecute;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class BungeeExecuteCommand extends AbstractCommand {

    // <--[command]
    // @Name BungeeExecute
    // @Syntax bungeeexecute [<command>]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, DepenizenBungee
    // @Required 1
    // @Stable stable
    // @Short Executes a command on the BungeeCord proxy server.
    // @Author Morphan1

    // @Description
    // This command allows you to execute a BungeeCord command, as if it
    // was typed into the proxy server's console.

    // @Tags
    // None

    // @Usage
    // Send an alert message through BungeeCord.
    // - bungeeexecute "alert Hey! Listen!"

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("command")) {
                scriptEntry.addObject("command", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("command")) {
            throw new InvalidArgumentsException("Must specify a command to execute!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element command = scriptEntry.getElement("command");

        dB.report(scriptEntry, getName(), command.debug());

        BungeeSupport.getSocketClient().trySend(new ClientPacketOutExecute(command.asString()));
    }
}
