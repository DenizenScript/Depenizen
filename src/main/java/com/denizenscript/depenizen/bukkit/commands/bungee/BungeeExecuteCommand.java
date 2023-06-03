package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.scripts.commands.generator.ArgDefaultNull;
import com.denizenscript.denizencore.scripts.commands.generator.ArgLinear;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.PacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ExecuteCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.ExecutePlayerCommandPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class BungeeExecuteCommand extends AbstractCommand {

    public BungeeExecuteCommand() {
        setName("bungeeexecute");
        setSyntax("bungeeexecute [<command>] (as:<player>)");
        setRequiredArguments(1, 2);
        autoCompile();
    }

    // <--[command]
    // @Name BungeeExecute
    // @Syntax bungeeexecute [<command>] (as:<player>)
    // @Group Depenizen
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    // @Required 1
    // @Maximum 2
    // @Short Runs a command on the Bungee proxy server, or a player.
    //
    // @Description
    // This command runs a command on the Bungee proxy server. Works similarly to "execute as_server".
    // If you specify "as:", you can use a PlayerTag or a UUID to automatically execute as that player, similar to "execute as_player".
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run the 'alert' bungee command.
    // - bungeeexecute "alert Network restart in 5 minutes..."
    //
    // -->

    public static void autoExecute(@ArgLinear @ArgName("command") String command,
                            @ArgPrefixed @ArgName("as") @ArgDefaultNull String asPlayer) {
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot BungeeExecute: bungee is not connected!");
            return;
        }
        PacketOut packet;
        if (asPlayer == null || asPlayer.length() == 0) {
            packet = new ExecuteCommandPacketOut(command);
        }
        else {
            if (asPlayer.startsWith("p@")) {
                asPlayer = asPlayer.substring("p@".length());
            }
            if (asPlayer.length() != 36) {
                Debug.echoError("Invalid UUID '" + asPlayer + "'!");
                return;
            }
            packet = new ExecutePlayerCommandPacketOut(asPlayer, command);
        }
        BungeeBridge.instance.sendPacket(packet);
        BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
    }
}
