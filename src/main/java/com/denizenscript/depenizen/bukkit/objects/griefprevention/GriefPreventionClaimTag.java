package com.denizenscript.depenizen.bukkit.objects.griefprevention;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.UUID;

public class GriefPreventionClaimTag implements ObjectTag, Adjustable {

    // <--[ObjectType]
    // @name GriefPreventionClaimTag
    // @prefix gpclaim
    // @base ElementTag
    // @format
    // The identity format for claims is <claim_id>
    // For example, 'gpclaim@1234'.
    //
    // @plugin Depenizen, GriefPrevention
    // @description
    // A GriefPreventionClaimTag represents a GriefPrevention claim.
    //
    // -->

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean matches(String id) {
        return valueOf(id) != null;
    }

    public static GriefPreventionClaimTag valueOf(String id) {
        return valueOf(id, null);
    }

    @Fetchable("gpclaim")
    public static GriefPreventionClaimTag valueOf(String id, TagContext context) {
        long claimID;
        id = id.replace("gpclaim@", "");
        try {
            claimID = Long.parseLong(id);
        }
        catch (NumberFormatException e) {
            return null;
        }
        Claim claim = dataStore.getClaim(claimID);
        if (claim == null) {
            return null;
        }
        return new GriefPreventionClaimTag(claim);
    }

    public GriefPreventionClaimTag(Claim claim) {
        this.claim = claim;
    }

    private String prefix;
    Claim claim;

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String getPrefix() {
        return prefix;
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
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.id>
        // @returns ElementTag(Number)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's ID.
        // -->
        if (attribute.startsWith("id")) {
            return new ElementTag(claim.getID()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.managers>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's managers.
        // -->
        if (attribute.startsWith("managers")) {
            ListTag managers = new ListTag();
            for (String manager : claim.managers) {
                managers.addObject(new PlayerTag(UUID.fromString(manager)));
            }
            return managers.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.trusted>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's trusted.
        // -->
        if (attribute.startsWith("trusted")) {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.addObject(new PlayerTag(UUID.fromString(trust)));
            }
            return trusted.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.builders>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's builders.
        // -->
        if (attribute.startsWith("builders")) {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.addObject(new PlayerTag(UUID.fromString(trust)));
            }
            return trusted.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.containers>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's containers.
        // -->
        if (attribute.startsWith("containers")) {
            ListTag trusted = new ListTag();
            ArrayList<String> c = new ArrayList<>();
            claim.getPermissions(new ArrayList<>(), c, new ArrayList<>(), new ArrayList<>());
            for (String container : c) {
                trusted.addObject(new PlayerTag(UUID.fromString(container)));
            }
            return trusted.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.accessors>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's accessors.
        // -->
        if (attribute.startsWith("accessors")) {
            ListTag trusted = new ListTag();
            ArrayList<String> a = new ArrayList<>();
            claim.getPermissions(new ArrayList<>(), new ArrayList<>(), a, new ArrayList<>());
            for (String access : a) {
                trusted.addObject(new PlayerTag(UUID.fromString(access)));
            }
            return trusted.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.owner>
        // @returns ObjectTag
        // @mechanism GriefPreventionClaimTag.owner
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's owner.
        // Can be "Admin" or a PlayerTag.
        // -->
        else if (attribute.startsWith("owner")) {
            if (claim.isAdminClaim()) {
                return new ElementTag("Admin").getObjectAttribute(attribute.fulfill(1));
            }
            return new PlayerTag(claim.ownerID)
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.cuboid>
        // @returns CuboidTag
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's cuboid area.
        // -->
        else if (attribute.startsWith("cuboid")) {
            LocationTag lower = new LocationTag(claim.getLesserBoundaryCorner());
            LocationTag upper = new LocationTag(claim.getGreaterBoundaryCorner());
            upper.setY(upper.getWorld().getMaxHeight());
            return new CuboidTag(lower, upper).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.world>
        // @returns WorldTag
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the world this GriefPreventionClaim is in.
        // -->
        else if (attribute.startsWith("world")) {
            return new WorldTag(claim.getLesserBoundaryCorner().getWorld());
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_adminclaim>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether GriefPreventionClaim is an Admin Claim.
        // -->
        else if (attribute.startsWith("is_adminclaim") || attribute.startsWith("is_admin_claim")) {
            return new ElementTag(claim.isAdminClaim()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.chunks>
        // @returns ListTag(ChunkTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns a list of all chunks in the GriefPreventionClaim.
        // -->
        else if (attribute.startsWith("chunks")) {
            ListTag chunks = new ListTag();
            for (Chunk chunk : claim.getChunks()) {
                chunks.addObject(new ChunkTag(chunk));
            }
            return chunks.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.can_siege[<player>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether the GriefPreventionClaim can siege the player.
        // -->
        else if (attribute.startsWith("can_siege") && attribute.hasParam()) {
            PlayerTag defender = attribute.paramAsType(PlayerTag.class);
            if (defender == null || defender.getPlayerEntity() == null) {
                return null;
            }
            return new ElementTag(claim.canSiege(defender.getPlayerEntity())).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_sieged>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether the GriefPreventionClaim is currently under siege.
        // -->
        else if (attribute.startsWith("is_sieged")) {
            return new ElementTag(claim.siegeData != null).getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        mechanism.echoError("Cannot apply Properties to a GriefPreventionClaim!");
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object GriefPreventionClaimTag
        // @name owner
        // @input PlayerTag/ElementTag
        // @plugin Depenizen, GriefPrevention
        // @description
        // Sets the owner of the GriefPreventionClaim.
        // Accepts PlayerTag or "admin" to set as admin claim.
        // @tags
        // <GriefPreventionClaimTag.owner>
        // -->
        if (mechanism.matches("owner")) {
            try {
                if (PlayerTag.matches(mechanism.getValue().asString())) {
                    PlayerTag player = mechanism.valueAsType(PlayerTag.class);
                    dataStore.changeClaimOwner(claim, player.getUUID());
                }
                else if (CoreUtilities.equalsIgnoreCase(mechanism.getValue().asString(), "admin")) {
                    dataStore.changeClaimOwner(claim, null);
                }
            }
            catch (Exception e) {
                mechanism.echoError("Unable to transfer ownership of claim: " + this.identify() + ".");
            }
        }

        // <--[mechanism]
        // @object GriefPreventionClaimTag
        // @name depth
        // @input ElementTag(Number)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Sets the protection depth of the GriefPreventionClaim.
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
}
