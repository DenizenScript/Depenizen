package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.RunScriptPacketOut;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.dB;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.dList;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

import java.util.UUID;

public class BungeeRunCommand extends AbstractCommand {

    // <--[command]
    // @Name BungeeRun
    // @Syntax bungeerun [<server>|...] [<script name>] (def:<definition>|...)
    // @Group Depenizen
    // @Plugin Depenizen, BungeeCord
    // @Required 2
    // @Short Runs a task script on another server.
    //
    // @Description
    // This command runs a task on another server on the Bungee network. Works similarly to the 'run' command.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run a simple task on another server.
    // - bungeerun lobby my_script def:32
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : ArgumentHelper.interpretArguments(scriptEntry.aHArgs)) {
            if (!scriptEntry.hasObject("def")
                    && arg.matchesPrefix("def")) {
                scriptEntry.addObject("def", arg.asElement());
            }
            else if (!scriptEntry.hasObject("servers")) {
                scriptEntry.addObject("servers", arg.asType(dList.class));
            }
            else if (!scriptEntry.hasObject("script_name")) {
                scriptEntry.addObject("script_name", arg.asElement());
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("script_name")) {
            throw new InvalidArgumentsException("Must define a SCRIPT to be run.");
        }
        if (!scriptEntry.hasObject("servers")) {
            throw new InvalidArgumentsException("Must define servers to run the script on.");
        }
        scriptEntry.defaultObject("def", new Element(""));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        Element def = scriptEntry.getElement("def");
        dList servers = scriptEntry.getdObject("servers");
        Element scriptName = scriptEntry.getElement("script_name");
        if (scriptEntry.dbCallShouldDebug()) {
            dB.report(scriptEntry, getName(), def.debug() + servers.debug() + scriptName.debug());
        }
        if (!BungeeBridge.instance.connected) {
            dB.echoError("Cannot BungeeRun: bungee is not connected!");
            return;
        }
        RunScriptPacketOut packetScript = new RunScriptPacketOut();
        packetScript.playerUUID = Utilities.entryHasPlayer(scriptEntry) ?
                Utilities.getEntryPlayer(scriptEntry).getOfflinePlayer().getUniqueId()
                : new UUID(0, 0);
        packetScript.scriptName = scriptName.asString();
        packetScript.defs = def.asString();
        for (String server : servers) {
            RedirectPacketOut packet = new RedirectPacketOut(server, packetScript);
            BungeeBridge.instance.sendPacket(packet);
            BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
        }
    }
}
