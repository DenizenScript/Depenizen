package com.denizenscript.depenizen.bukkit.extensions.askyblock;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

import java.util.UUID;

public class ASkyBlockPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static ASkyBlockPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ASkyBlockPlayerExtension((dPlayer) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "skyblock"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ASkyBlockPlayerExtension(dPlayer player) {
        this.player = player;
        skyblock = api.getIslandOwnedBy(player.getOfflinePlayer().getUniqueId());
    }

    ASkyBlockAPI api = ASkyBlockAPI.getInstance();
    dPlayer player;
    Island skyblock;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("skyblock")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.skyblock.has_skyblock>
            // @returns Element(Boolean)
            // @description
            // Returns whether the player has a skyblock.
            // @Plugin Depenizen, A SkyBlock
            // -->
            if (attribute.startsWith("has_skyblock")) {
                return new Element(api.hasIsland(player.getOfflinePlayer().getUniqueId()))
                        .getAttribute(attribute.fulfill(1));
            }

            if (skyblock != null) {
                // <--[tag]
                // @attribute <p@player.skyblock.center>
                // @returns dLocation
                // @description
                // Returns the centre of the player's skyblock.
                // @Plugin Depenizen, A SkyBlock
                // -->
                if (attribute.startsWith("center") || attribute.startsWith("centre")) {
                    return new dLocation(skyblock.getCenter()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.skyblock.spawn_point>
                // @returns dLocation
                // @description
                // Returns the spawnpoint of the player's skyblock.
                // @Plugin Depenizen, A SkyBlock
                // -->
                else if (attribute.startsWith("spawn_point")) {
                    return new dLocation(skyblock.getSpawnPoint()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.skyblock.members>
                // @returns dList(dPlayer)
                // @description
                // Returns a list of members of the player's skyblock.
                // @Plugin Depenizen, A SkyBlock
                // -->
                else if (attribute.startsWith("members")) {
                    dList m = new dList();
                    for (UUID u : skyblock.getMembers()) {
                        m.add(new dPlayer(u).identify());
                    }
                    return m.getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.skyblock.level>
                // @returns Element(Number)
                // @description
                // Returns the level of the player's skyblock.
                // @Plugin Depenizen, A SkyBlock
                // -->
                else if (attribute.startsWith("level")) {
                    return new Element(api.getIslandLevel(player.getOfflinePlayer().getUniqueId()))
                            .getAttribute(attribute.fulfill(1));
                }
            }
        }
        return null;
    }

}
