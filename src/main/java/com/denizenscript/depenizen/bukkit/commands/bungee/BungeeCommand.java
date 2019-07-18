package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.RunCommandsPacketOut;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.BracedCommand;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BungeeCommand extends BracedCommand {

    // <--[command]
    // @Name Bungee
    // @Syntax bungee [<server>|...] [<commands>]
    // @Group Depenizen
    // @Plugin Depenizen, BungeeCord
    // @Required 1
    // @Short Runs a set of commands on another server.
    //
    // @Description
    // This command runs a set of commands on another server on the Bungee network.
    // Tags will be parsed on the remote server, but definitions from the originating queue will be used.
    // The linked player will be available on the remote server if that server has ever seen the player.
    // Generally, prefer <@link command BungeeRun>.
    //
    // @Tags
    // None
    //
    // @Usage
    // Use to run a simple announce command on another server;
    // - bungee lobby:
    //   - announce "Wow! Stuff is happening!"
    //
    // -->

    @Override
    public void onEnable() {
        setBraced();
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : ArgumentHelper.interpretArguments(scriptEntry.aHArgs)) {
            if (!scriptEntry.hasObject("servers")) {
                scriptEntry.addObject("servers", arg.asType(ListTag.class));
            }
            else if (arg.matchesOne("{")) {
                break;
            }
            else {
                arg.reportUnhandled();
            }
        }
        if (!scriptEntry.hasObject("servers")) {
            throw new InvalidArgumentsException("Must define servers to run the script on.");
        }
        scriptEntry.addObject("braces", getBracedCommands(scriptEntry));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ListTag servers = scriptEntry.getObjectTag("servers");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), servers.debug());
        }
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot Bungee command: bungee is not connected!");
            return;
        }
        List<BracedCommand.BracedData> bdlist = (List<BracedData>) scriptEntry.getObject("braces");
        if (bdlist == null || bdlist.isEmpty()) {
            com.denizenscript.denizencore.utilities.debugging.Debug.echoError(scriptEntry.getResidingQueue(), "Empty braces (internal)!");
            return;
        }
        List<ScriptEntry> bracedCommandsList = bdlist.get(0).value;
        if (bracedCommandsList == null || bracedCommandsList.isEmpty()) {
            com.denizenscript.denizencore.utilities.debugging.Debug.echoError(scriptEntry.getResidingQueue(), "Empty braces!");
            return;
        }
        StringBuilder toSend = new StringBuilder();
        for (ScriptEntry entry : bracedCommandsList) {
            toSend.append(stringify(entry)).append("\n");
        }
        StringBuilder defNames = new StringBuilder();
        StringBuilder defValues = new StringBuilder();
        for (Map.Entry<String, ObjectTag> def : scriptEntry.getResidingQueue().getAllDefinitions().entrySet()) {
            defNames.append(escape(def.getKey())).append("\n");
            defValues.append(escape(def.getValue().toString())).append("\n");
        }
        RunCommandsPacketOut packetScript = new RunCommandsPacketOut(toSend.toString(),
                defNames.toString() + "\r" + defValues.toString(), scriptEntry.shouldDebug(),
                Utilities.entryHasPlayer(scriptEntry) ?
                Utilities.getEntryPlayer(scriptEntry).getOfflinePlayer().getUniqueId()
                : new UUID(0, 0));
        for (String server : servers) {
            RedirectPacketOut packet = new RedirectPacketOut(server, packetScript);
            BungeeBridge.instance.sendPacket(packet);
            BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
        }
    }

    public static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    }

    public static String stringify(Object obj) {
        if (obj instanceof ScriptEntry) {
            ScriptEntry entry = (ScriptEntry) obj;
            if (entry.internal.insideList == null) {
                return escape(entry.internal.originalLine);
            }
            StringBuilder result = new StringBuilder();
            result.append(escape(entry.internal.originalLine)).append("\r");
            for (Object subCommand : entry.internal.insideList) {
                result.append(stringify(subCommand)).append("\r");
            }
            return result.toString();
        }
        else if (obj instanceof Map) {
            Map<Object, Object> valueMap = (Map<Object, Object>) obj;
            StringBuilder result = new StringBuilder();
            Object cmdLine = valueMap.keySet().toArray()[0];
            result.append(escape(cmdLine.toString())).append("\r");
            List<Object> inside = (List<Object>) valueMap.get(cmdLine);
            for (Object subCommand : inside) {
                result.append(stringify(subCommand)).append("\r");
            }
            return result.toString();
        }
        else {
            return escape(obj.toString());
        }
    }
}
