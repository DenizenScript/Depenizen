package com.denizenscript.depenizen.bukkit.properties.playerpoints;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.PlayerPointsBridge;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import org.black_ixx.playerpoints.PlayerPoints;

public class PlayerPointsPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "PlayerPointsPlayer";
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static PlayerPointsPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlayerPointsPlayerProperties((PlayerTag) object);
        }
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "playerpoints_points"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private PlayerPointsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.playerpoints_points>
        // @returns ElementTag(Number)
        // @description
        // Returns the amount of points the player has. Only works on online players.
        // @Plugin Depenizen, PlayerPoints
        // -->
        if (attribute.startsWith("playerpoints_points")) {
            return new ElementTag(((PlayerPoints) PlayerPointsBridge.instance.plugin).getAPI()
                    .look(player.getOfflinePlayer().getUniqueId())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}