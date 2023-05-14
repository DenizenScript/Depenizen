package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultText;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ExecuteCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;

public class BungeeExecuteCommand extends AbstractCommand {

    public BungeeExecuteCommand() {
        setName("bungeeexecute");
        setSyntax("bungeeexecute ({as_server}/as_player) [<command>]");
        setRequiredArguments(1, 2);
        autoCompile();
    }

    // <--[command]
    // @Name BungeeExecute
    // @Syntax bungeeexecute ({as_server}/as_player) [<command>]
    // @Group Depenizen
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    // @Required 1
    // @Maximum 2
    // @Short Runs a command on the Bungee proxy server.
    //
    // @Description
    // Runs a command on the Bungee proxy server, works similarly to <@link command execute>.
    // The command is run as the proxy by default, use 'as_player' to run it as the linked player.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run the 'alert' bungee command as the proxy.
    // - bungeeexecute "alert Network restart in 5 minutes..."
    // @Usage
    // Use to run the 'perms' bungee command as the linked player.
    // - bungeeexecute as_player perms
    //
    // -->

    public enum Executor {AS_SERVER, AS_PLAYER}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("executor") @ArgDefaultText("as_server") Executor executor,
                                   @ArgName("command") @ArgLinear ElementTag command) {
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot BungeeExecute: bungee is not connected!");
            return;
        }
        ExecuteCommandPacketOut packet = switch (executor) {
            case AS_SERVER -> new ExecuteCommandPacketOut(command.asString(), null);
            case AS_PLAYER -> {
                PlayerTag player = Utilities.getEntryPlayer(scriptEntry);
                if (player == null) {
                    throw new InvalidArgumentsRuntimeException("Must have a linked player to use 'as_player'.");
                }
                yield new ExecuteCommandPacketOut(command.asString(), player.getUUID());
            }
        };
        BungeeBridge.instance.sendPacket(packet);
        BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
    }
}
