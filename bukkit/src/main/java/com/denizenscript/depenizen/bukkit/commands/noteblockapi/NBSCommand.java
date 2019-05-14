package com.denizenscript.depenizen.bukkit.commands.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.io.File;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

public class NBSCommand extends AbstractCommand {
    // <--[command]
    // @Name nbs
    // @Syntax nbs [play/stop] (file:<file path>) [targets:<entity>|...]
    // @Group Depenizen
    // @Plugin DepenizenBukkit, NoteBlockAPI
    // @Required 2
    // @Short Plays or stops a noteblock song.

    // @Description
    // Plays a .nbs file for the targets, being players specified.
    // If no targets are specified, the target will be the player in the queue.
    // Note block song files are created using NoteBlockStudio or other programs.
    // The file path starts in the denizen folder: /plugins/Denizen/

    // @Tags
    // <p@player.nbs_is_playing>

    // @Usage
    // Use to play a song to the linked player in a queue.
    // - nbs play file:MySong

    // @Usage
    // Use to play a song to everyone online.
    // - nbs play file:MySong targets:<player.list_online_players>

    // @Usage
    // Use to stop the current song playing for the linked player in a queue.
    // - nbs stop

    // @Usage
    // Use to stop the current song playing for all online players.
    // - nbs stop targets:<player.list_online_players>

    // -->

    private enum Action {PLAY, STOP}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {

            if (!scriptEntry.hasObject("targets")
                    && arg.matchesPrefix("targets", "targets")
                    && arg.matchesArgumentList(dPlayer.class)) {
                scriptEntry.addObject("targets", arg.asType(dList.class).filter(dPlayer.class));
            }

            else if (!scriptEntry.hasObject("file")
                    && arg.matchesPrefix("file")) {
                scriptEntry.addObject("file", arg.asElement());
            }

            else if (!scriptEntry.hasObject("action")
                    && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", arg.asElement());
            }

            else {
                arg.reportUnhandled();
            }

        }
        if (!scriptEntry.hasObject("action")) {
            throw new InvalidArgumentsException("Action not specified! (play/stop)");
        }
        else if (!scriptEntry.hasObject("targets")) {
            if (((BukkitScriptEntryData) scriptEntry.entryData).hasPlayer()) {
                scriptEntry.addObject("targets", Collections.singletonList(((BukkitScriptEntryData) scriptEntry.entryData).getPlayer()));
            }
            else {
                throw new InvalidArgumentsException("Must specify players to add, remove or spectate!");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

        Element file = scriptEntry.getdObject("file");
        Element action = scriptEntry.getdObject("action");
        List<dPlayer> targets = (List<dPlayer>) scriptEntry.getObject("targets");

        // Report to dB
        dB.report(scriptEntry, getName(), action.debug()
                + aH.debugList("targets", targets)
                + (file != null ? file.debug() : ""));

        if (targets == null || targets.isEmpty()) {
            dB.echoError(scriptEntry.getResidingQueue(), "Targets not found!");
            return;
        }

        if (action.asString().equalsIgnoreCase("play")) {
            if (file == null) {
                dB.echoError(scriptEntry.getResidingQueue(), "File not specified!");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            Song s = NBSDecoder.parse(new File(directory + "/plugins/Denizen/" + file + ".nbs"));
            SongPlayer sp = new RadioSongPlayer(s);
            sp.setAutoDestroy(true);
            for (dPlayer p : targets) {
                sp.addPlayer(p.getPlayerEntity());
            }
            sp.setPlaying(true);
        }

        else if (action.asString().equalsIgnoreCase("stop")) {
            for (dPlayer p : targets) {
                NoteBlockAPI.stopPlaying(p.getPlayerEntity());
            }
        }


    }
}
