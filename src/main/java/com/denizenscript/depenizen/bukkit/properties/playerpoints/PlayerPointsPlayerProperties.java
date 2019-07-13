package com.denizenscript.depenizen.bukkit.properties.playerpoints;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.bridges.PlayerPointsBridge;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.dObject;
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

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PlayerPointsPlayerProperties getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlayerPointsPlayerProperties((dPlayer) object);
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

    private PlayerPointsPlayerProperties(dPlayer player) {
        this.player = player;
    }

    dPlayer player;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.playerpoints_points>
        // @returns Element(Number)
        // @description
        // Returns the amount of points the player has. Only works on online players.
        // @Plugin Depenizen, PlayerPoints
        // -->
        if (attribute.startsWith("playerpoints_points")) {
            return new Element(((PlayerPoints) PlayerPointsBridge.instance.plugin).getAPI()
                    .look(player.getOfflinePlayer().getUniqueId())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
