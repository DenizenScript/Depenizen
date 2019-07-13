package com.denizenscript.depenizen.bukkit.properties.noteblockapi;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;

public class NoteBlockAPIPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "NoteBlockAPIPlayer";
    }

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static NoteBlockAPIPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoteBlockAPIPlayerProperties((dPlayer) object);
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

    private NoteBlockAPIPlayerProperties(dPlayer player) {
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
