package com.denizenscript.depenizen.bukkit.commands.noteblockapi;

import com.denizenscript.denizencore.objects.Argument;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.io.File;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

public class NBSCommand extends AbstractCommand {

    public NBSCommand() {
        setName("nbs");
        setSyntax("nbs [play/stop] (file:<file path>) (targets:<entity>|...)");
        setRequiredArguments(1, 3);
    }

    // <--[command]
    // @Name nbs
    // @Syntax nbs [play/stop] (file:<file path>) (targets:<entity>|...)
    // @Group Depenizen
    // @Plugin Depenizen, NoteBlockAPI
    // @Required 1
    // @Maximum 3
    // @Short Plays or stops a noteblock song.
    //
    // @Description
    // Plays a .nbs file for the targets, being players specified.
    // If no targets are specified, the target will be the player in the queue.
    // Note block song files are created using NoteBlockStudio or other programs.
    // The file path starts in the denizen folder: /plugins/Denizen/
    //
    // @Tags
    // <PlayerTag.nbs_is_playing>
    //
    // @Usage
    // Use to play a song to the linked player in a queue.
    // - nbs play file:MySong
    //
    // @Usage
    // Use to play a song to everyone online.
    // - nbs play file:MySong targets:<server.online_players>
    //
    // @Usage
    // Use to stop the current song playing for the linked player in a queue.
    // - nbs stop
    //
    // @Usage
    // Use to stop the current song playing for all online players.
    // - nbs stop targets:<server.online_players>
    //
    // -->

    private enum Action {PLAY, STOP}

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        for (Argument arg : scriptEntry.getProcessedArgs()) {
            if (!scriptEntry.hasObject("targets")
                    && arg.matchesPrefix("targets", "targets")
                    && arg.matchesArgumentList(PlayerTag.class)) {
                scriptEntry.addObject("targets", arg.asType(ListTag.class).filter(PlayerTag.class, scriptEntry));
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
            if (Utilities.entryHasPlayer(scriptEntry)) {
                scriptEntry.addObject("targets", Collections.singletonList(Utilities.getEntryPlayer(scriptEntry)));
            }
            else {
                throw new InvalidArgumentsException("Must specify players to add, remove or spectate!");
            }
        }
    }

    @Override
    public void execute(ScriptEntry scriptEntry) {
        ElementTag file = scriptEntry.getObjectTag("file");
        ElementTag action = scriptEntry.getObjectTag("action");
        List<PlayerTag> targets = (List<PlayerTag>) scriptEntry.getObject("targets");
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), action, ArgumentHelper.debugList("targets", targets), file);
        }
        if (targets == null || targets.isEmpty()) {
            Debug.echoError(scriptEntry.getResidingQueue(), "Targets not found!");
            return;
        }
        if (action.asString().equalsIgnoreCase("play")) {
            if (file == null) {
                Debug.echoError(scriptEntry.getResidingQueue(), "File not specified!");
                return;
            }
            String directory = URLDecoder.decode(System.getProperty("user.dir"));
            Song s = NBSDecoder.parse(new File(directory + "/plugins/Denizen/" + file + ".nbs"));
            SongPlayer sp = new RadioSongPlayer(s);
            sp.setAutoDestroy(true);
            for (PlayerTag p : targets) {
                sp.addPlayer(p.getPlayerEntity());
            }
            sp.setPlaying(true);
        }
        else if (action.asString().equalsIgnoreCase("stop")) {
            for (PlayerTag p : targets) {
                NoteBlockAPI.stopPlaying(p.getPlayerEntity());
            }
        }
    }
}
