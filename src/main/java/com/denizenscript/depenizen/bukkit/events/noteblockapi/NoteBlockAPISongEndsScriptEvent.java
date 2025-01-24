package com.denizenscript.depenizen.bukkit.events.noteblockapi;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.nio.file.Path;

public class NoteBlockAPISongEndsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // noteblockapi song (<'song'>) ends
    //
    // @Cancellable true
    //
    // @Triggers when a song playing through NoteBlockAPI ends.
    //
    // @Context
    // <context.song> returns the file name of the song.
    //
    // @Plugin Depenizen, NoteBlockAPI
    //
    // @Group Depenizen
    //
    // -->

    public NoteBlockAPISongEndsScriptEvent() {
        registerCouldMatcher("noteblockapi song (<'song'>) ends");
    }

    public SongEndEvent event;

    @Override
    public boolean matches(ScriptPath path) {
        String songName = path.eventArgLowerAt(2);
        if (!songName.equals("ends") && !songName.equals(CoreUtilities.toLowerCase(getSongFileName()))) {
            return false;
        }
        return true;
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(null, null);
    }

    public String getSongFileName() {
        try {
            Path songPath = event.getSongPlayer().getSong().getPath().toPath();
            Path denizenPath = Denizen.getInstance().getDataFolder().toPath();
            String finalPath = denizenPath.relativize(songPath).toString();
            if (finalPath.endsWith(".nbs")) {
                finalPath = finalPath.substring(0, finalPath.length() - ".nbs".length());
            }
            return finalPath;
        }
        catch (Throwable ex) {
            Debug.echoError(ex);
            return null;
        }
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "song" -> new ElementTag(getSongFileName());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onSongEndEvent(SongEndEvent event) {
        this.event = event;
        fire(event);
    }
}
