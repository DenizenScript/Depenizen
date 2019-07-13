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

    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "griefprevention"
    };

    public static final String[] handledMechs = new String[] {
            "bonus_blocks", "normal_blocks"
    };

    private GriefPreventionPlayerProperties(PlayerTag player) {
        this.player = player;
        data = dataStore.getPlayerData(player.getOfflinePlayer().getUniqueId());
    }

    PlayerData data;
    PlayerTag player;

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("grief_prevention")
                || attribute.startsWith("gp")
                || attribute.startsWith("griefprevention")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <PlayerTag.griefprevention.list_claims>
            // @returns ListTag(GriefPreventionClaim)
            // @description
            // Returns a list of all claims the player has.
            // @Plugin Depenizen, GriefPrevention
            // -->
            if (attribute.startsWith("list_claims")) {
                ListTag claims = new ListTag();
                for (Claim claim : data.getClaims()) {
                    claims.add(new GriefPreventionClaimTag(claim).identify());
                }
                return claims.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <PlayerTag.griefprevention.claims>
            // @returns ElementTag(Number)
            // @description
            // Returns the number of claims the player has in GriefPrevention.
            // @Plugin Depenizen, GriefPrevention
            // -->
            else if (attribute.startsWith("claims")) {
                return new ElementTag(data.getClaims().size()).getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("blocks")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.remaining>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of claim blocks the player has left.
                // @Plugin Depenizen, GriefPrevention
                // -->
                if (attribute.startsWith("remaining")) {
                    return new ElementTag(data.getRemainingClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.bonus>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of bonus claim blocks the player has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else if (attribute.startsWith("bonus")) {
                    return new ElementTag(data.getBonusClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks.total>
                // @returns ElementTag(Number)
                // @description
                // Returns the total number of claim blocks the player has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else if (attribute.startsWith("total")) {
                    return new ElementTag(data.getAccruedClaimBlocks() + data.getBonusClaimBlocks())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <PlayerTag.griefprevention.blocks>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of normal claim blocks the payer has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else {
                    return new ElementTag(data.getAccruedClaimBlocks()).getAttribute(attribute);
                }
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        if (mechanism.matches("bonus_blocks") && mechanism.requireInteger()) {
            data.setBonusClaimBlocks(mechanism.getValue().asInt());
        }

        if (mechanism.matches("normal_blocks") && mechanism.requireInteger()) {
            data.setAccruedClaimBlocks(mechanism.getValue().asInt());
        }
    }
}
