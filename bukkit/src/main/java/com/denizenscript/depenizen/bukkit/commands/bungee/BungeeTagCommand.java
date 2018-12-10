package com.denizenscript.depenizen.bukkit.commands.bungee;

import com.denizenscript.depenizen.bukkit.objects.bungee.dServer;
import com.denizenscript.depenizen.bukkit.support.bungee.BungeeSupport;
import com.denizenscript.depenizen.common.socket.client.packet.ClientPacketOutTag;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.scripts.commands.Holdable;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.HashMap;
import java.util.Map;

public class BungeeTagCommand extends AbstractCommand implements Holdable {

    // <--[command]
    // @Name BungeeTag
    // @Syntax bungeetag [<tag>] [server:<server>]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, DepenizenBungee
    // @Required 2
    // @Stable stable
    // @Short Gets tags from other servers on the BungeeCord network. Requires you ~wait for it.
    // @Author Morphan1

    // @Description
    // This command allows you to request and receive data from Denizen tags on other servers
    // connected to BungeeCord. The tag argument should be written just like a normal tag; you
    // don't have to change anything about it. All definitions in the current queue are also
    // sent, so you may use them in the tag as usual.
    //
    // NOTE: You MUST ~wait for the command to use it. This means DO NOT use "- bungeetag", but
    // rather, "- ~bungeetag". This is so that the data can be correctly sent and received.

    // @Tags
    // <entry[entryName].result> returns the value of the tag as parsed on the other server.

    // @Usage
    // Use to check a player's experience on another server.
    // - define player <player>
    // - ~bungeetag <def[player].xp> server:Survival save:xp
    // - narrate "You have <entry[xp].result> XP in Survival."

    // -->

    public BungeeTagCommand() {
        setParseArgs(false);
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getOriginalArguments())) {

            if (!scriptEntry.hasObject("server")
                    && arg.matchesPrefix("server", "s")) {
                scriptEntry.addObject("server", dServer.valueOf(TagManager.tag(arg.getValue(),
                        DenizenAPI.getCurrentInstance().getTagContext(scriptEntry))));
            }

            else if (!scriptEntry.hasObject("tag")) {
                scriptEntry.addObject("tag", new Element(arg.raw_value));
            }

        }

        if (!scriptEntry.hasObject("tag") || !scriptEntry.hasObject("server")) {
            throw new InvalidArgumentsException("Must specify a tag and a server!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        Element tag = scriptEntry.getElement("tag");
        dServer server = scriptEntry.getdObject("server");

        dB.report(scriptEntry, getName(), tag.debug() + server.debug());

        if (BungeeSupport.isSocketRegistered()) {
            if (!scriptEntry.shouldWaitFor()) {
                dB.echoError("You MUST ~wait for this command!");
                return;
            }
            int id = nextId++;
            waitingEntries.put(id, scriptEntry);
            Map<String, dObject> definitions = scriptEntry.getResidingQueue().getAllDefinitions();
            Map<String, String> toSend = new HashMap<String, String>();
            for (Map.Entry<String, dObject> def : definitions.entrySet()) {
                toSend.put(def.getKey(), def.getValue().toString());
            }
            BungeeSupport.getSocketClient().trySend(new ClientPacketOutTag(server.getName(), id, tag.asString(),
                    scriptEntry.shouldDebug(), scriptEntry.shouldDebug(),
                    toSend));
        }
        else {
            dB.echoError("Server is not connected to a BungeeCord Socket.");
        }
    }

    private static int nextId = 0;

    private static Map<Integer, ScriptEntry> waitingEntries = new HashMap<Integer, ScriptEntry>();

    public static void returnTag(int id, String value) {
        ScriptEntry scriptEntry = waitingEntries.get(id);
        scriptEntry.addObject("result", ObjectFetcher.pickObjectFor(value));
        scriptEntry.setFinished(true);
        waitingEntries.remove(id);
    }
}
