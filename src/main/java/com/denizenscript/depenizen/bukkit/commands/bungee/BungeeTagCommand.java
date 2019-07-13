package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.bungee.BungeeBridge;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.KeepAlivePacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.RedirectPacketOut;
import com.denizenscript.depenizen.bukkit.bungee.packets.out.redirectable.ReadTagPacketOut;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.dB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BungeeTagCommand extends AbstractCommand implements Holdable {

    // <--[command]
    // @Name BungeeTag
    // @Syntax bungeetag [server:<server>] [<tag>]
    // @Group Depenizen
    // @Plugin Depenizen, BungeeCord
    // @Required 2
    // @Short Gets the result of a tag from another server on the Bungee network.
    //
    // @Description
    // This command gets the result of a tag from another server on the Bungee network.
    // Tags will be parsed on the remote server, but definitions from the originating queue will be used.
    // The linked player will be available on the remote server if that server has ever seen the player.
    //
    // This command must be held with the '~' prefix. The queue will then wait for the result.
    //
    // @Tags
    // <entry[saveName].result> returns the result of the parsed tag.
    //
    // @Usage
    // Use to read a simple tag from another server.
    // - ~bungeetag server:lobby <server.motd> save:motd
    // - narrate "The lobby's MOTD is <entry[motd].result>"
    //
    // -->

    @Override
    public void onEnable() {
        setParseArgs(false);
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (String arg : scriptEntry.getArguments()) {
            if (!scriptEntry.hasObject("server")
                    && arg.startsWith("server:")) {
                scriptEntry.addObject("server", new Element(TagManager.tag(arg.substring("server:".length()),
                        scriptEntry.entryData.getTagContext())));
            }
            else if (!scriptEntry.hasObject("tag")) {
                scriptEntry.addObject("tag", new Element(arg));
            }
            else {
                dB.echoError('\'' + arg + "' is an unknown argument!");
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
        entry.addObject("result", new Element(result));
        entry.setFinished(true);
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        Element tag = scriptEntry.getElement("tag");
        Element server = scriptEntry.getElement("server");
        if (scriptEntry.dbCallShouldDebug()) {
            dB.report(scriptEntry, getName(), tag.debug() + server.debug());
        }
        if (!BungeeBridge.instance.connected) {
            dB.echoError("Cannot BungeeTag: bungee is not connected!");
            scriptEntry.setFinished(true);
            return;
        }
        int newId = currentId++;
        StringBuilder defNames = new StringBuilder();
        StringBuilder defValues = new StringBuilder();
        for (Map.Entry<String, dObject> def : scriptEntry.getResidingQueue().getAllDefinitions().entrySet()) {
            defNames.append(BungeeCommand.escape(def.getKey())).append("\n");
            defValues.append(BungeeCommand.escape(def.getValue().toString())).append("\n");
        }
        ReadTagPacketOut packetTag = new ReadTagPacketOut();
        packetTag.playerUUID = Utilities.entryHasPlayer(scriptEntry) ?
                Utilities.getEntryPlayer(scriptEntry).getOfflinePlayer().getUniqueId()
                : new UUID(0, 0);
        packetTag.tag = tag.asString();
        packetTag.id = newId;
        packetTag.defs = defNames.toString() + "\r" + defValues.toString();
        RedirectPacketOut packet = new RedirectPacketOut(server.asString(), packetTag);
        BungeeBridge.instance.sendPacket(packet);
        BungeeBridge.instance.sendPacket(new KeepAlivePacketOut());
        waitingEntries.put(newId, scriptEntry);
    }
}
