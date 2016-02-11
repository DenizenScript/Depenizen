package net.gnomeffinway.depenizen.objects.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.*;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;

public class GriefPreventionClaim implements dObject, Adjustable {

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static GriefPreventionClaim valueOf(String id) {
        return valueOf(id, null);
    }

    @Fetchable("gpclaim")
    public static GriefPreventionClaim valueOf(String id, TagContext context) {
        long claimID;
        try {
            claimID = Long.valueOf(id);
        }
        catch (NumberFormatException e) {
            return null;
        }
        Claim claim = dataStore.getClaim(claimID);
        if (claim == null) {
            return null;
        }
        return new GriefPreventionClaim(claim);
    }

    public GriefPreventionClaim(Claim claim) {
        this.claim = claim;
    }

    private String prefix;
    Claim claim;

    @Override
    public dObject setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "GriefPreventionClaim";
    }

    @Override
    public String identify() {
        return "gpclaim@" + claim.getID();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // TODO: Meta
        if (attribute.startsWith("id")) {
            return new Element(claim.getID()).getAttribute(attribute.fulfill(1));
        }

        // TODO: Meta
        else if (attribute.startsWith("owner")) {
            return new dPlayer(dPlayer.getAllPlayers().get(CoreUtilities.toLowerCase(claim.getOwnerName())))
                    .getAttribute(attribute.fulfill(1));
        }

        // TODO: Meta
        else if (attribute.startsWith("cuboid")) {
            dCuboid cuboid = new dCuboid();
            cuboid.addPair(new dLocation(claim.getLesserBoundaryCorner()), new dLocation(claim.getGreaterBoundaryCorner()));
            return cuboid.getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        Element value = mechanism.getValue();

        // TODO: Meta
        if (mechanism.matches("owner") && mechanism.requireObject(dPlayer.class)) {
            dPlayer player = value.asType(dPlayer.class);
            try {
                dataStore.changeClaimOwner(claim, player.getOfflinePlayer().getUniqueId());
            }
            catch (Exception e) {
                dB.echoError("Unable to transfer ownership of claim: " + this.identify() + " to " + player.identify());
            }
        }

        // Iterate through this object's properties' mechanisms
        for (Property property : PropertyParser.getProperties(this)) {
            property.adjust(mechanism);
            if (mechanism.fulfilled()) {
                break;
            }
        }

        if (!mechanism.fulfilled()) {
            mechanism.reportInvalid();
        }
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        dB.echoError("Cannot apply Properties to a GriefPreventionClaim!");
    }
}
