package com.denizenscript.depenizen.bukkit.properties.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;

public class GriefPreventionPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "GriefPreventionPlayer";
    }

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static GriefPreventionPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "griefprevention"
    };

    public static final String[] handledMechs = new String[] {
            "bonus_blocks", "normal_blocks"
    };

    public GriefPreventionPlayerProperties(PlayerTag player) {
        this.player = player;
        data = dataStore.getPlayerData(player.getUUID());
    }

    PlayerData data;
    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("grief_prevention")
                || attribute.startsWith("gp")
                || attribute.startsWith("griefprevention")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.griefprevention.list_claims>
            // @returns ListTag(GriefPreventionClaimTag)
            // @plugin Depenizen, GriefPrevention
            // @description
            // Returns a list of all claims the player has.
            // -->
            if (attribute.startsWith("list_claims")) {
                ListTag claims = new ListTag();
                for (Claim claim : data.getClaims()) {
                    claims.addObject(new GriefPreventionClaimTag(claim));
                }
                return claims.getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.griefprevention.claims>
            // @returns ElementTag(Number)
            // @plugin Depenizen, GriefPrevention
            // @description
            // Returns the number of claims the player has in GriefPrevention.
            // -->
            else if (attribute.startsWith("claims")) {
                return new ElementTag(data.getClaims().size()).getObjectAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("blocks")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.remaining>
                // @returns ElementTag(Number)
                // @plugin Depenizen, GriefPrevention
                // @description
                // Returns the number of claim blocks the player has left.
                // -->
                if (attribute.startsWith("remaining")) {
                    return new ElementTag(data.getRemainingClaimBlocks()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.bonus>
                // @returns ElementTag(Number)
                // @plugin Depenizen, GriefPrevention
                // @mechanism PlayerTag.bonus_blocks
                // @description
                // Returns the number of bonus claim blocks the player has.
                // -->
                else if (attribute.startsWith("bonus")) {
                    return new ElementTag(data.getBonusClaimBlocks()).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.total>
                // @returns ElementTag(Number)
                // @plugin Depenizen, GriefPrevention
                // @description
                // Returns the total number of claim blocks the player has.
                // -->
                else if (attribute.startsWith("total")) {
                    return new ElementTag(data.getAccruedClaimBlocks() + data.getBonusClaimBlocks())
                            .getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks>
                // @returns ElementTag(Number)
                // @plugin Depenizen, GriefPrevention
                // @mechanism PlayerTag.normal_blocks
                // @description
                // Returns the number of normal claim blocks the player has.
                // -->
                else {
                    return new ElementTag(data.getAccruedClaimBlocks()).getObjectAttribute(attribute);
                }
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object PlayerTag
        // @name bonus_blocks
        // @input ElementTag(Number)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Sets the player's bonus claim blocks.
        // @tags
        // <PlayerTag.griefprevention.blocks.bonus>
        // -->
        if (mechanism.matches("bonus_blocks") && mechanism.requireInteger()) {
            data.setBonusClaimBlocks(mechanism.getValue().asInt());
        }

        // <--[mechanism]
        // @object PlayerTag
        // @name normal_blocks
        // @input ElementTag(Number)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Sets the player's accrued claim blocks.
        // @tags
        // <PlayerTag.griefprevention.blocks>
        // -->
        if (mechanism.matches("normal_blocks") && mechanism.requireInteger()) {
            data.setAccruedClaimBlocks(mechanism.getValue().asInt());
        }
    }
}
