package com.denizenscript.depenizen.bukkit.commands.clientizen;

import com.denizenscript.depenizen.bukkit.support.clientizen.ClientizenSupport;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.HashMap;
import java.util.Map;

public class ClientRunCommand extends AbstractCommand {

    // <--[command]
    // @Name ClientRun
    // @Syntax clientrun [<script_name>] (def:<name>|<value>|...)
    // @Group Depenizen
    // @Plugin DepenizenBukkit
    // @Required 1
    // @Short Runs a client script on a client, assuming they have the Forge mod 'Clientizen'.

    // @Description
    // This command is used to run a script on the Minecraft client by utilizing the Denizen2 Forge mod
    // implementation, Clientizen.
    // The script must be sent beforehand, either automatically when the player joined or sent by
    // the <@link command ClientScripts> command.

    // @Tags
    // None

    // @Usage
    // Runs the script 'server_update_gui' on the client.
    // - clientrun server_update_gui def:text|Hello

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("definitions")
                    && arg.matchesPrefix("d", "def", "define")) {
                scriptEntry.addObject("definitions", arg.asType(dList.class));
            }

            else if (!scriptEntry.hasObject("script_name")) {
                scriptEntry.addObject("script_name", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("script_name")) {
            throw new InvalidArgumentsException("Must specify a script name!");
        }

        BukkitScriptEntryData data = (BukkitScriptEntryData) scriptEntry.entryData;
        if (!data.hasPlayer() || !data.getPlayer().isOnline()) {
            throw new InvalidArgumentsException("This command must have an online player attached!");
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        Element scriptName = scriptEntry.getElement("script_name");
        dList definitions = scriptEntry.getdObject("definitions");

        dB.report(scriptEntry, getName(), scriptName.debug()
                + (definitions != null ? definitions.debug() : ""));

        Map<String, String> finalDefs = new HashMap<String, String>();
        if (definitions != null) {
            if (definitions.size() % 2 != 0) {
                dB.echoError("Uneven number of elements in definitions list!");
                return;
            }
            for (int i = 0; i < definitions.size(); i++) {
                finalDefs.put(definitions.get(i), definitions.get(++i));
            }
        }

        BukkitScriptEntryData data = (BukkitScriptEntryData) scriptEntry.entryData;
        ClientizenSupport.runScript(data.getPlayer().getPlayerEntity(), scriptName.asString(), finalDefs);
    }
}
