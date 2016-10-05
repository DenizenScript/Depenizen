package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class BungeeReconnectCommand extends AbstractCommand {

    // <--[command]
    // @Name BungeeReconnect
    // @Syntax bungeereconnect
    // @Plugin DepenizenBukkit, DepenizenBungee
    // @Required 0
    // @Stable stable
    // @Short Attempts to reconnect to the BungeeCord socket server.
    // @Author Morphan1

    // @Description
    // This command allows you to try to manually reconnect to the server.
    // Note that you don't typically need to use this, as the client will automatically
    // attempt to reconnect on its own. The main use of this is manual handling.

    // @Tags
    // None

    // @Usage
    // Try to reconnect to the server.
    // - bungeereconnect

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        // No args
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        dB.report(scriptEntry, getName(), "");

        if (BungeeSupport.isSocketConnected()) {
            dB.echoError("Server is already connected to the BungeeCord Socket.");
        }
        else if (BungeeSupport.isSocketReconnecting()) {
            dB.echoError("Server is already trying to reconnect to the BungeeCord Socket.");
        }
        else {
            BungeeSupport.attemptReconnect();
        }
    }
}
