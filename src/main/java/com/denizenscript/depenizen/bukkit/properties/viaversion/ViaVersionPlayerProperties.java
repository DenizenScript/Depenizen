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

    private ViaVersionPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        //Getting the ViaVersion API object


        // <--[tag]
        // @attribute <PlayerTag.viaversion>
        // @returns ElementTag(Number)
        // @description
        // Returns the protocol version number of the player's client.
        // See <@link url https://wiki.vg/Protocol_version_numbers as a reference list.>
        // @Plugin Depenizen, ViaVersion
        // -->
        if (attribute.startsWith("viaversion")) {
            ViaAPI api = Via.getAPI();
            return new ElementTag(api.getPlayerVersion(player.getOfflinePlayer().getUniqueId()))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
