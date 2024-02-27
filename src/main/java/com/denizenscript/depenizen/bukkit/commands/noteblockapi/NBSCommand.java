package com.denizenscript.depenizen.bukkit.commands.noteblockapi;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.scripts.commands.generator.*;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

public class NBSCommand extends AbstractCommand {

    public NBSCommand() {
        setName("nbs");
        setSyntax("nbs [play/stop] (file:<file_path>) (targets:<entity>|...) (at:<location>) (distance:<#>/{16})");
        setRequiredArguments(1, 5);
        autoCompile();
    }

    // <--[command]
    // @Name nbs
    // @Syntax nbs [play/stop] (file:<file_path>) (targets:<entity>|...) (at:<location>) (distance:<#>/{16})
    // @Group Depenizen
    // @Plugin Depenizen, NoteBlockAPI
    // @Required 1
    // @Maximum 5
    // @Short Plays or stops a noteblock song.
    //
    // @Description
    // Plays a .nbs file for the targets, being players specified.
    // If no targets are specified, the target will be the player in the queue.
    // Optionally specify the location to play the song from using the 'at' argument. Players must still be added using 'targets' in order to hear the song when they are within range.
    // Setting 'distance' will change the range in blocks that a player must be in to hear the song. Numbers below 16 will decrease volume, not decrease range. Numbers greater than 16 will increase range.
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
    // @Usage
    // Use to play a song only for the linked player in a queue at a specified location.
    // - nbs play file:MySong at:<[my_location]>
    //
    // @Usage
    // Use to play a song to everyone online if they are 30 blocks away from a specified location.
    // - nbs play file:MySong targets:<server.online_players> at:<[my_location]> distance:30
    // -->

    public enum Action {PLAY, STOP}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("action") Action action,
                                   @ArgName("file") @ArgPrefixed @ArgDefaultNull String file,
                                   @ArgName("targets") @ArgPrefixed @ArgDefaultNull @ArgSubType(PlayerTag.class) List<PlayerTag> targets,
                                   @ArgName("at") @ArgPrefixed @ArgDefaultNull LocationTag location,
                                   @ArgName("distance") @ArgPrefixed @ArgDefaultText("16") int distance) {
        if (targets == null) {
            if (Utilities.entryHasPlayer(scriptEntry)) {
                targets = List.of(Utilities.getEntryPlayer(scriptEntry));
            }
            else {
                throw new InvalidArgumentsRuntimeException("Must specify players to add or remove!");
            }
        }
        switch (action) {
            case PLAY -> {
                if (file == null) {
                    Debug.echoError(scriptEntry, "File not specified!");
                    return;
                }
                File songFile = new File(Denizen.getInstance().getDataFolder(), file + ".nbs");
                if (!Utilities.canReadFile(songFile)) {
                    Debug.echoError("Cannot read from that file path due to security settings in Denizen/config.yml.");
                    return;
                }
                Song s = NBSDecoder.parse(songFile);
                SongPlayer sp = new RadioSongPlayer(s);
                if (location != null) {
                    sp = new PositionSongPlayer(s);
                    ((PositionSongPlayer) sp).setTargetLocation(location);
                    ((PositionSongPlayer) sp).setDistance(distance);
                }
                for (PlayerTag p : targets) {
                    sp.addPlayer(p.getPlayerEntity());
                }
                sp.setAutoDestroy(true);
                sp.setPlaying(true);
            }
            case STOP -> {
                for (PlayerTag p : targets) {
                    NoteBlockAPI.stopPlaying(p.getPlayerEntity());
                }
            }
        }
    }
}
