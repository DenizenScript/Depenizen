package net.gnomeffinway.depenizen.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.BracedCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.gnomeffinway.depenizen.Depenizen;
import net.gnomeffinway.depenizen.objects.bungee.dServer;
import net.gnomeffinway.depenizen.support.bungee.packets.ClientPacketOutScript;

import java.util.List;

public class BungeeCommand extends BracedCommand {

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("server")
                    && !scriptEntry.hasObject("all-servers")
                    && arg.matchesArgumentType(dServer.class)) {
                scriptEntry.addObject("server", arg.asType(dServer.class));
            }

            else if (!scriptEntry.hasObject("server")
                    && !scriptEntry.hasObject("all-servers")
                    && arg.matches("all")) {
                scriptEntry.addObject("all-servers", new Element(true));
            }

            else if (arg.matches("{")) {
                scriptEntry.addObject("braces", getBracedCommands(scriptEntry));
                break;
            }

        }

        if (!scriptEntry.hasObject("server") && !scriptEntry.hasObject("all-servers"))
            throw new InvalidArgumentsException("Must specify a valid server or 'ALL'.");

        if (!scriptEntry.hasObject("braces"))
            throw new InvalidArgumentsException("Must have braces.");

        scriptEntry.defaultObject("all-servers", new Element(false));
    }

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element all = scriptEntry.getElement("all-servers");
        dServer server = scriptEntry.getdObject("server");
        List<ScriptEntry> bracedCommands = ((List<BracedData>) scriptEntry.getObject("braces")).get(0).value;

        dB.report(scriptEntry, getName(), (server != null ? server.debug() : "") + all.debug());

        if (Depenizen.getCurrentInstance().isSocketConnected()) {
            ClientPacketOutScript packet = new ClientPacketOutScript(all.asBoolean() ? "ALL" : server.getName(),
                    bracedCommands, scriptEntry.getResidingQueue().getAllDefinitions());
            Depenizen.getCurrentInstance().getSocketClient().send(packet);
        }
        else {
            ByteArrayDataOutput data = ByteStreams.newDataOutput();
            data.writeUTF("Depenizen");
            data.writeUTF("dummy data");

            ((BukkitScriptEntryData) scriptEntry.entryData).getPlayer().getPlayerEntity()
                    .sendPluginMessage(Depenizen.getCurrentInstance(), "BungeeCord", data.toByteArray());
        }
    }
}
