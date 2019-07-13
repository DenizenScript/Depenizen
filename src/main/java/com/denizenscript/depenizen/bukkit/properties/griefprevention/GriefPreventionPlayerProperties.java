package com.denizenscript.depenizen.bukkit.properties.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaim;

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
        return object instanceof dPlayer;
    }

    public static GriefPreventionPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionPlayerProperties((dPlayer) object);
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

    private GriefPreventionPlayerProperties(dPlayer player) {
        this.player = player;
        data = dataStore.getPlayerData(player.getOfflinePlayer().getUniqueId());
    }

    PlayerData data;
    dPlayer player;

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
            // @attribute <p@player.griefprevention.list_claims>
            // @returns ListTag(GriefPreventionClaim)
            // @description
            // Returns a list of all claims the player has.
            // @Plugin Depenizen, GriefPrevention
            // -->
            if (attribute.startsWith("list_claims")) {
                ListTag claims = new ListTag();
                for (Claim claim : data.getClaims()) {
                    claims.add(new GriefPreventionClaim(claim).identify());
                }
                return claims.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.griefprevention.claims>
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
                // @attribute <p@player.griefprevention.blocks.remaining>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of claim blocks the player has left.
                // @Plugin Depenizen, GriefPrevention
                // -->
                if (attribute.startsWith("remaining")) {
                    return new ElementTag(data.getRemainingClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks.bonus>
                // @returns ElementTag(Number)
                // @description
                // Returns the number of bonus claim blocks the player has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else if (attribute.startsWith("bonus")) {
                    return new ElementTag(data.getBonusClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks.total>
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
                // @attribute <p@player.griefprevention.blocks>
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
