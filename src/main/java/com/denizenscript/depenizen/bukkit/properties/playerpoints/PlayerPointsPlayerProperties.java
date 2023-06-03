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

    public static final String[] handledTags = new String[] {
            "playerpoints_points"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public PlayerPointsPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.playerpoints_points>
        // @returns ElementTag(Number)
        // @plugin Depenizen, PlayerPoints
        // @description
        // Returns the amount of points the player has. Only works on online players.
        // -->
        if (attribute.startsWith("playerpoints_points")) {
            return new ElementTag(((PlayerPoints) PlayerPointsBridge.instance.plugin).getAPI()
                    .look(player.getUUID())).getObjectAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
