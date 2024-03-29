package com.denizenscript.depenizen.bukkit.properties.askyblock;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

import java.util.UUID;

public class ASkyBlockPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "ASkyBlockPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static ASkyBlockPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new ASkyBlockPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "skyblock"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public ASkyBlockPlayerProperties(PlayerTag player) {
        this.player = player;
        skyblock = api.getIslandOwnedBy(player.getUUID());
    }

    ASkyBlockAPI api = ASkyBlockAPI.getInstance();
    PlayerTag player;
    Island skyblock;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("skyblock")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.skyblock.has_skyblock>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, A SkyBlock
            // @description
            // Returns whether the player has a skyblock.
            // -->
            if (attribute.startsWith("has_skyblock")) {
                return new ElementTag(api.hasIsland(player.getUUID()))
                        .getObjectAttribute(attribute.fulfill(1));
            }

            if (skyblock != null) {
                // <--[tag]
                // @attribute <PlayerTag.skyblock.center>
                // @returns LocationTag
                // @plugin Depenizen, A SkyBlock
                // @description
                // Returns the centre of the player's skyblock.
                // -->
                if (attribute.startsWith("center") || attribute.startsWith("centre")) {
                    return new LocationTag(skyblock.getCenter()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skyblock.spawn_point>
                // @returns LocationTag
                // @plugin Depenizen, A SkyBlock
                // @description
                // Returns the spawnpoint of the player's skyblock.
                // -->
                else if (attribute.startsWith("spawn_point")) {
                    return new LocationTag(skyblock.getSpawnPoint()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skyblock.members>
                // @returns ListTag(PlayerTag)
                // @plugin Depenizen, A SkyBlock
                // @description
                // Returns a list of members of the player's skyblock.
                // -->
                else if (attribute.startsWith("members")) {
                    ListTag m = new ListTag();
                    for (UUID u : skyblock.getMembers()) {
                        m.addObject(new PlayerTag(u));
                    }
                    return m.getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.skyblock.level>
                // @returns ElementTag(Number)
                // @plugin Depenizen, A SkyBlock
                // @description
                // Returns the level of the player's skyblock.
                // -->
                else if (attribute.startsWith("level")) {
                    return new ElementTag(api.getIslandLevel(player.getUUID()))
                            .getObjectAttribute(attribute.fulfill(1));
                }
            }
        }
        return null;
    }
}
