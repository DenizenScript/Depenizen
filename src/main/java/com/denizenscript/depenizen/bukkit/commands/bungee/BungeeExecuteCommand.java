package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ExecuteCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class BungeeExecuteCommand extends AbstractCommand {

    public BungeeExecuteCommand() {
        setName("bungeeexecute");
        setSyntax("bungeeexecute [<command>]");
        setRequiredArguments(1, 1);
    }

    // <--[command]
    // @Name BungeeExecute
    // @Syntax bungeeexecute [<command>]
    // @Group Depenizen
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    // @Required 1
    // @Maximum 1
    // @Short Runs a command on the Bungee proxy server.
    //
    // @Description
    // This command runs a command on the Bungee proxy server. Works similarly to "execute as_server".
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run the 'alert' bungee command.
    // - bungeeexecute "alert Network restart in 5 minutes..."
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry) {
            if (!scriptEntry.hasObject("command")) {
                scriptEntry.addObject("command", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("command")) {
            throw new InvalidArgumentsException("Must define a COMMAND to be run.");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag command = scriptEntry.getElement("command");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), command);
        }
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot BungeeExecute: bungee is not connected!");
            return;
        }
        ExecuteCommandPacketOut packet = new ExecuteCommandPacketOut(command.asString());
        BungeeBridge.instance.sendPacket(packet);
        BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
    }
}
