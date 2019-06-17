package com.denizenscript.depenizen.bukkit.extensions.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaim;

public class GriefPreventionPlayerExtension extends dObjectExtension {

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static GriefPreventionPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionPlayerExtension((dPlayer) object);
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

    private GriefPreventionPlayerExtension(dPlayer player) {
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
            // @returns dList(GriefPreventionClaim)
            // @description
            // Returns a list of all claims the player has.
            // @Plugin Depenizen, GriefPrevention
            // -->
            if (attribute.startsWith("list_claims")) {
                dList claims = new dList();
                for (Claim claim : data.getClaims()) {
                    claims.add(new GriefPreventionClaim(claim).identify());
                }
                return claims.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.griefprevention.claims>
            // @returns Element(Number)
            // @description
            // Returns the number of claims the player has in GriefPrevention.
            // @Plugin Depenizen, GriefPrevention
            // -->
            else if (attribute.startsWith("claims")) {
                return new Element(data.getClaims().size()).getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("blocks")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks.remaining>
                // @returns Element(Number)
                // @description
                // Returns the number of claim blocks the player has left.
                // @Plugin Depenizen, GriefPrevention
                // -->
                if (attribute.startsWith("remaining")) {
                    return new Element(data.getRemainingClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks.bonus>
                // @returns Element(Number)
                // @description
                // Returns the number of bonus claim blocks the player has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else if (attribute.startsWith("bonus")) {
                    return new Element(data.getBonusClaimBlocks()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks.total>
                // @returns Element(Number)
                // @description
                // Returns the total number of claim blocks the player has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else if (attribute.startsWith("total")) {
                    return new Element(data.getAccruedClaimBlocks() + data.getBonusClaimBlocks())
                            .getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.griefprevention.blocks>
                // @returns Element(Number)
                // @description
                // Returns the number of normal claim blocks the payer has.
                // @Plugin Depenizen, GriefPrevention
                // -->
                else {
                    return new Element(data.getAccruedClaimBlocks()).getAttribute(attribute);
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
