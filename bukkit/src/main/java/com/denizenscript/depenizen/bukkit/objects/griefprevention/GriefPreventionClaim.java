package com.denizenscript.depenizen.bukkit.objects.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.objects.dChunk;
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
import org.bukkit.Chunk;

public class GriefPreventionClaim implements dObject, Adjustable {

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean matches(String id) {
        return valueOf(id) != null;
    }

    public static GriefPreventionClaim valueOf(String id) {
        return valueOf(id, null);
    }

    @Fetchable("gpclaim")
    public static GriefPreventionClaim valueOf(String id, TagContext context) {
        long claimID;
        id = id.replace("gpclaim@", "");
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
    public String toString() {
        return identify();
    }

    public String simple() {
        return String.valueOf(claim.getID());
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

        // <--[tag]
        // @attribute <gpclaim@gpclaim.id>
        // @returns Element(Number)
        // @description
        // Returns the GriefPreventionClaim's ID.
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        if (attribute.startsWith("id")) {
            return new Element(claim.getID()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <gpclaim@gpclaim.owner>
        // @returns dPlayer/Element
        // @description
        // Returns the GriefPreventionClaim's owner.
        // Can be "Admin" or a dPlayer.
        // @mechanism GriefPreventionClaim.owner
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        else if (attribute.startsWith("owner")) {
            if (claim.isAdminClaim()) {
                return new Element("Admin").getAttribute(attribute.fulfill(1));
            }
            return new dPlayer(claim.ownerID)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <gpclaim@gpclaim.cuboid>
        // @returns dCuboid
        // @description
        // Returns the GriefPreventionClaim's cuboid area.
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        else if (attribute.startsWith("cuboid")) {
            dLocation lower = new dLocation(claim.getLesserBoundaryCorner());
            lower.setY(0);
            dLocation upper = new dLocation(claim.getGreaterBoundaryCorner());
            upper.setY(255);
            return new dCuboid(lower, upper).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <gpclaim@gpclaim.is_adminclaim>
        // @returns Element(Boolean)
        // @description
        // Returns whether GriefPreventionClaim is an Admin Claim.
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        else if (attribute.startsWith("is_adminclaim") || attribute.startsWith("is_admin_claim")) {
            return new Element(claim.isAdminClaim()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <gpclaim@gpclaim.chunks>
        // @returns dList(dChunk)
        // @description
        // Returns a list of all chunks in the GriefPreventionClaim.
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        else if (attribute.startsWith("chunks")) {
            dList chunks = new dList();
            for (Chunk chunk : claim.getChunks()) {
                chunks.add(new dChunk(chunk).identify());
            }
            return chunks.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <gpclaim@gpclaim.can_siege[<player>]>
        // @returns Element(Boolean)
        // @description
        // Returns whether the GriefPreventionClaim can siege the player.
        // @Plugin DepenizenBukkit, GriefPrevention
        // -->
        else if (attribute.startsWith("can_siege") && attribute.hasContext(1)) {
            dPlayer defender = dPlayer.valueOf(attribute.getContext(1));
            if (defender == null || defender.getPlayerEntity() == null) {
                return null;
            }
            return new Element(claim.canSiege(defender.getPlayerEntity())).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object GriefPreventionClaim
        // @name owner
        // @input dPlayer/Element
        // @description
        // Sets the owner of the GriefPreventionClaim.
        // Accepts dPlayer or "admin" to set as admin claim.
        // @tags
        // <gpclaim@gpclaim.owner>
        // -->
        if (mechanism.matches("owner")) {
            try {
                if (dPlayer.matches(mechanism.getValue().asString())) {
                    dPlayer player = dPlayer.valueOf(mechanism.getValue().asString());
                    dataStore.changeClaimOwner(claim, player.getOfflinePlayer().getUniqueId());
                }
                else if (CoreUtilities.toLowerCase(mechanism.getValue().asString()).equals("admin")) {
                    dataStore.changeClaimOwner(claim, null);
                }
            }
            catch (Exception e) {
                dB.echoError("Unable to transfer ownership of claim: " + this.identify() + ".");
            }
        }

        // <--[mechanism]
        // @object GriefPreventionClaim
        // @name depth
        // @input Element(Number)
        // @description
        // Sets the protection depth of the GriefPreventionClaim.
        // @tags
        // None
        // -->
        if (mechanism.matches("depth") && mechanism.requireInteger()) {
            dataStore.extendClaim(claim, mechanism.getValue().asInt());
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
