package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.RunScriptPacketOut;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

import java.util.UUID;

public class BungeeRunCommand extends AbstractCommand {

    public BungeeRunCommand() {
        setName("bungeerun");
        setSyntax("bungeerun [<server>|...] [<script name>] (def:<definition>|...)");
        setRequiredArguments(2, 3);
    }

    // <--[command]
    // @Name BungeeRun
    // @Syntax bungeerun [<server>|...] [<script name>] (def:<definition>|...)
    // @Group Depenizen
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    // @Required 2
    // @Maximum 3
    // @Short Runs a task script on another server.
    //
    // @Description
    // This command runs a task on another server on the Bungee network. Works similarly to the 'run' command.
    //
    // @Tags
    // <bungee.list_servers>
    //
    // @Usage
    // Use to run a simple task on another server.
    // - bungeerun lobby my_script def:32
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry.getProcessedArgs()) {
            if (!scriptEntry.hasObject("def")
                    && arg.matchesPrefix("def")) {
                scriptEntry.addObject("def", arg.asElement());
            }
            else if (!scriptEntry.hasObject("servers")) {
                scriptEntry.addObject("servers", arg.asType(ListTag.class));
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
        scriptEntry.defaultObject("def", new ElementTag(""));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag def = scriptEntry.getElement("def");
        ListTag servers = scriptEntry.getObjectTag("servers");
        ElementTag scriptName = scriptEntry.getElement("script_name");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), def.debug() + servers.debug() + scriptName.debug());
        }
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot BungeeRun: bungee is not connected!");
            return;
        }
        RunScriptPacketOut packetScript = new RunScriptPacketOut();
        packetScript.playerUUID = Utilities.entryHasPlayer(scriptEntry) ?
                Utilities.getEntryPlayer(scriptEntry).getUUID()
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
