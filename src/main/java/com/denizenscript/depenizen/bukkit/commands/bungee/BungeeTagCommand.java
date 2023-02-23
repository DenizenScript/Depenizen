package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.denizencore.utilities.text.StringHolder;
import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.ReadTagPacketOut;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BungeeTagCommand extends AbstractCommand implements Holdable {

    public BungeeTagCommand() {
        setName("bungeetag");
        setSyntax("bungeetag [server:<server>] [<tags>]");
        setRequiredArguments(2, 2);
        setParseArgs(false);
    }

    // <--[command]
    // @Name BungeeTag
    // @Syntax bungeetag [server:<server>] [<tags>]
    // @Group Depenizen
    // @Plugin Depenizen, DepenizenBungee, BungeeCord
    // @Required 2
    // @Maximum 2
    // @Short Parses tags on another server on a Bungee network and returns the results for this server to process.
    //
    // @Description
    // This command parses tags on another server on a Bungee network and returns the results for this server to process.
    //
    // As a more technical explanation: All commands in Denizen parse tags in any input arguments prior to processing them further.
    // This command skips that step, and sends the raw tags out to the specified server to then have that server perform the actual tag processing,
    // when then gets sent back and allows the command to complete.
    //
    // Tags will be parsed on the remote server, but definitions from the originating queue will be used.
    // The linked player will be available on the remote server if that server has ever seen the player.
    //
    // This command must be held with the '~' prefix. The queue will then wait for the result.
    //
    // @Tags
    // <entry[saveName].result> returns the result of the parsed tag.
    // <bungee.list_servers>
    //
    // @Usage
    // Use to read a simple tag from another server.
    // - ~bungeetag server:lobby <server.motd> save:motd
    // - narrate "The lobby's MOTD is <entry[motd].result>"
    //
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (String arg : scriptEntry.getOriginalArguments()) {
            if (!scriptEntry.hasObject("server")
                    && arg.startsWith("server:")) {
                scriptEntry.addObject("server", new ElementTag(TagManager.tag(arg.substring("server:".length()),
                        scriptEntry.getContext())));
            }
            else if (!scriptEntry.hasObject("tag")) {
                scriptEntry.addObject("tag", new ElementTag(arg));
            }
            else {
                Debug.echoError('\'' + arg + "' is an unknown argument!");
            }
        }
        if (!scriptEntry.hasObject("tag")) {
            throw new InvalidArgumentsException("Must define a TAG to be read.");
        }
        if (!scriptEntry.hasObject("server")) {
            throw new InvalidArgumentsException("Must define a server to run the tag on.");
        }
        if (!scriptEntry.shouldWaitFor()) {
            throw new InvalidArgumentsException("The BungeeTag command must be ~waited for!");
        }
    }

    public static Map<Integer, ScriptEntry> waitingEntries = new HashMap<>();

    public static int currentId = 1;

    public static void handleResult(int id, String result) {
        ScriptEntry entry = waitingEntries.remove(id);
        if (entry == null) {
            return;
        }
        entry.saveObject("result", new ElementTag(result));
        entry.setFinished(true);
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag tag = scriptEntry.getElement("tag");
        ElementTag server = scriptEntry.getElement("server");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), tag, server);
        }
        if (!BungeeBridge.instance.connected) {
            Debug.echoError("Cannot BungeeTag: bungee is not connected!");
            scriptEntry.setFinished(true);
            return;
        }
        int newId = currentId++;
        StringBuilder defNames = new StringBuilder();
        StringBuilder defValues = new StringBuilder();
        for (Map.Entry<StringHolder, ObjectTag> def : scriptEntry.getResidingQueue().getAllDefinitions().map.entrySet()) {
            defNames.append(BungeeCommand.escape(def.getKey().low)).append("\n");
            defValues.append(BungeeCommand.escape(def.getValue().toString())).append("\n");
        }
        ReadTagPacketOut packetTag = new ReadTagPacketOut();
        packetTag.playerUUID = Utilities.entryHasPlayer(scriptEntry) ?
                Utilities.getEntryPlayer(scriptEntry).getUUID()
                : new UUID(0, 0);
        packetTag.tag = tag.asString();
        packetTag.id = newId;
        packetTag.defs = defNames + "\r" + defValues;
        RedirectPacketOut packet = new RedirectPacketOut(server.asString(), packetTag);
        BungeeBridge.instance.sendPacket(packet);
        BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
        waitingEntries.put(newId, scriptEntry);
    }
}
