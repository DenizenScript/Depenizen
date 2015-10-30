package net.gnomeffinway.depenizen.commands.bungee;

import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.scripts.commands.Holdable;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.bungee.BungeeSupport;
import net.gnomeffinway.depenizen.support.bungee.packets.ClientPacketOutTag;

import java.util.HashMap;
import java.util.Map;

public class BungeeTagCommand extends AbstractCommand implements Holdable {

    public BungeeTagCommand(){
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
                scriptEntry.addObject("tag", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("tag") || !scriptEntry.hasObject("server")) {
            throw new InvalidArgumentsException("Must specify a tag and a server!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element tag = scriptEntry.getElement("tag");
        dServer server = scriptEntry.getdObject("server");

        dB.report(scriptEntry, getName(), tag.debug() + server.debug());

        if (BungeeSupport.isSocketConnected()) {
            if (!scriptEntry.shouldWaitFor()) {
                throw new CommandExecutionException("Currently, this command only works if you ~wait for it!");
            }
            int id = nextId++;
            waitingEntries.put(id, scriptEntry);
            BungeeSupport.getSocketClient().send(new ClientPacketOutTag(id, tag.asString(), server.getName()));
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
