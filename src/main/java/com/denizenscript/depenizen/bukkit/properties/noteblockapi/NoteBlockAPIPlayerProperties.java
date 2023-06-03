package com.denizenscript.depenizen.bukkit.properties.noteblockapi;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
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

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static NoteBlockAPIPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new NoteBlockAPIPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "nbs_is_playing"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public NoteBlockAPIPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.nbs_is_playing>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, NoteBlockAPI
        // @description
        // Returns true if the player is currently listening to a note block song.
        // -->
        if (attribute.startsWith("nbs_is_playing")) {
            return new ElementTag(NoteBlockAPI.isReceivingSong(player.getPlayerEntity())).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag value = mechanism.getValue();
    }
}
