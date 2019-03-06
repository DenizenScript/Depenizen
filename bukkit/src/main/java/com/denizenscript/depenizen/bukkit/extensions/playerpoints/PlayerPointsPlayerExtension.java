package com.denizenscript.depenizen.bukkit.extensions.playerpoints;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.support.plugins.PlayerPointsSupport;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.plugin.Plugin;

public class PlayerPointsPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static PlayerPointsPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new PlayerPointsPlayerExtension((dPlayer) object);
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

    private PlayerPointsPlayerExtension(dPlayer player) {
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
        // @Plugin DepenizenBukkit, PlayerPoints
        // -->
        if (attribute.startsWith("playerpoints_points")) {
            Plugin plugin = Support.getPlugin(PlayerPointsSupport.class);
            return new Element(((PlayerPoints) plugin).getAPI().look(player.getOfflinePlayer().getUniqueId())).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
