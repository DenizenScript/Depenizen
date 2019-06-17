package com.denizenscript.depenizen.bukkit.extensions.noteblockapi;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class NoteBlockAPIPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static NoteBlockAPIPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoteBlockAPIPlayerExtension((dPlayer) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "nbs_is_playing"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private NoteBlockAPIPlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.nbs_is_playing>
        // @returns Element(Boolean)
        // @description
        // Returns true if the player is currently listening to a note block song.
        // @Plugin Depenizen, NoteBlockAPI
        // -->
        if (attribute.startsWith("nbs_is_playing")) {
            return new Element(NoteBlockAPI.isReceivingSong(player.getPlayerEntity())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
