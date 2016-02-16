package net.gnomeffinway.depenizen.extensions.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.griefprevention.GriefPreventionClaim;

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

    private GriefPreventionPlayerExtension(dPlayer player) {
        this.player = player;
    }

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
            // @plugin Depenizen, GriefPrevention
            // -->
            if (attribute.startsWith("list_claims")) {
                dList claims = new dList();
                for (Claim claim : dataStore.getPlayerData(player.getOfflinePlayer().getUniqueId()).getClaims()) {
                    claims.add(new GriefPreventionClaim(claim).identify());
                }
                return claims.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.griefprevention.claims>
            // @returns Element(Number)
            // @description
            // Returns the number of claims the player has in GriefPrevention.
            // @plugin Depenizen, GriefPrevention
            // -->
            else if (attribute.startsWith("claims")) {
                return new Element(dataStore.getPlayerData(player.getOfflinePlayer().getUniqueId())
                        .getClaims().size()).getAttribute(attribute.fulfill(1));
            }

            else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();
    }
}
