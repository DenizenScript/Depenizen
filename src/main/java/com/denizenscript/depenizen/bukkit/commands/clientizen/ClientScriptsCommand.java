package com.denizenscript.depenizen.bukkit.commands.clientizen;

import com.denizenscript.depenizen.bukkit.support.clientizen.ClientizenSupport;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.Collections;
import java.util.List;

public class ClientScriptsCommand extends AbstractCommand {

    // <--[command]
    // @Name ClientScripts
    // @Syntax clientscripts [add/remove] [<file_name>|...] (players:<player>|...)
    // @Group Depenizen
    // @Plugin DepenizenBukkit
    // @Required 2
    // @Short Sends or removes client scripts, assuming they have the Forge mod 'Clientizen'.

    // @Description
    // This command allows dynamic control of loading and unloading client scripts.
    // The file name will never include subfolders, as no two files should share names. For example,
    // if you have a file at '/client_scripts/subfolder/myscript.yml', you would input simply
    // it as 'myscript.yml'.

    // @Tags
    // None

    // @Usage
    // Loads the script file "myscript.yml" for all online players.
    // - clientscripts add myscript.yml <server.list_online_players>

    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue().toUpperCase()));
            }

            else if (!scriptEntry.hasObject("players")
                    && arg.matchesPrefix("players")) {
                scriptEntry.addObject("players", arg.asType(dList.class).filter(dPlayer.class, scriptEntry));
            }

            else if (!scriptEntry.hasObject("files")) {
                scriptEntry.addObject("files", arg.asType(dList.class));
            }

        }

        if (!scriptEntry.hasObject("action") || !scriptEntry.hasObject("files")) {
            throw new InvalidArgumentsException("Must specify action and file names!");
        }

        if (!scriptEntry.hasObject("players")) {
            scriptEntry.addObject("players", Collections.singletonList(((BukkitScriptEntryData) scriptEntry.entryData).getPlayer()));
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        Action action = (Action) scriptEntry.getObject("action");
        dList files = scriptEntry.getdObject("files");
        List<dPlayer> players = (List<dPlayer>) scriptEntry.getObject("players");

        dB.report(scriptEntry, getName(), aH.debugObj("action", action)
                + files.debug()
                + aH.debugList("players", players));

        for (String file : files) {
            if (!ClientizenSupport.isScriptLoaded(file)) {
                dB.echoError("Failed to send scripts because '" + file + "' doesn't exist or isn't loaded!");
                return;
            }
        }

        switch (action) {
            case ADD:
                for (dPlayer player : players) {
                    ClientizenSupport.sendScripts(player.getPlayerEntity(), files);
                }
                break;
            case REMOVE:
                for (dPlayer player : players) {
                    ClientizenSupport.removeScripts(player.getPlayerEntity(), files);
                }
                break;
        }
    }

    private enum Action {
        ADD, REMOVE
    }
}
