package com.denizenscript.depenizen.bukkit.properties.viaversion;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.Via;

public class ViaVersionPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlayerViaVersion";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static ViaVersionPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ViaVersionPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "viaversion"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ViaVersionPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <PlayerTag.viaversion>
        // @returns ElementTag(Number)
        // @plugin Depenizen, ViaVersion
        // @description
        // Returns the protocol version number of the player's client.
        // See <@link url https://wiki.vg/Protocol_version_numbers> as a reference list.
        // -->
        if (attribute.startsWith("viaversion")) {
            ViaAPI api = Via.getAPI();
            return new ElementTag(api.getPlayerVersion(player.getUUID()))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
