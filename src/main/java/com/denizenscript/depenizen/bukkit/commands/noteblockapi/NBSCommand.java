package com.denizenscript.depenizen.bukkit.commands.noteblockapi;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.scripts.commands.generator.*;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
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
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBSCommand extends AbstractCommand implements Holdable, Listener {

    public NBSCommand() {
        setName("nbs");
        setSyntax("nbs [play/stop] (file:<file_path>) (targets:<entity>|...) (at:<location>) (distance:<#>/{16})");
        setRequiredArguments(1, 5);
        autoCompile();
        Bukkit.getServer().getPluginManager().registerEvents(this, Depenizen.instance);
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
    // The nbs command is ~waitable. Refer to <@link language ~waitable>.
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
    //
    // @Usage
    // Use to wait until the song finishes.
    // - ~nbs play file:MySong
    // -->

    public enum Action {PLAY, STOP}

    public static Map<SongPlayer, ScriptEntry> waitingForSongs = new HashMap<>();

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
                scriptEntry.setFinished(true);
                throw new InvalidArgumentsRuntimeException("Must specify players that can hear the song!");
            }
        }
        switch (action) {
            case PLAY -> {
                if (file == null) {
                    Debug.echoError("File not specified!");
                    scriptEntry.setFinished(true);
                    return;
                }
                File songFile = new File(Denizen.getInstance().getDataFolder(), file + ".nbs");
                if (!Utilities.canReadFile(songFile)) {
                    Debug.echoError("Cannot read from that file path due to security settings in Denizen/config.yml.");
                    scriptEntry.setFinished(true);
                    return;
                }
                Song s = NBSDecoder.parse(songFile);
                SongPlayer sp;
                if (location == null) {
                    sp = new RadioSongPlayer(s);
                }
                else {
                    sp = new PositionSongPlayer(s);
                    ((PositionSongPlayer) sp).setTargetLocation(location);
                    ((PositionSongPlayer) sp).setDistance(distance);
                }
                for (PlayerTag p : targets) {
                    sp.addPlayer(p.getPlayerEntity());
                }
                sp.setAutoDestroy(true);
                sp.setPlaying(true);
                if (scriptEntry.shouldWaitFor()) {
                    waitingForSongs.put(sp, scriptEntry);
                }
                else {
                   scriptEntry.setFinished(true);
                }
            }
            case STOP -> {
                for (PlayerTag p : targets) {
                    NoteBlockAPI.stopPlaying(p.getPlayerEntity());
                    scriptEntry.setFinished(true);
                }
            }
        }
    }

    @EventHandler
    public void onSongEnds(SongEndEvent event) {
        SongPlayer sp = event.getSongPlayer();
        ScriptEntry scriptEntry = waitingForSongs.remove(sp);
        if (scriptEntry != null) {
            scriptEntry.setFinished(true);
        }
    }
}
