package com.denizenscript.depenizen.bukkit.objects.griefprevention;

import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizen.objects.ChunkTag;
import com.denizenscript.denizen.objects.CuboidTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.UUID;

public class GriefPreventionClaimTag implements ObjectTag, Adjustable {

    // <--[language]
    // @name GriefPreventionClaimTag
    // @group Depenizen Object Types
    // @plugin Depenizen, GriefPrevention
    // @description
    // A GriefPreventionClaimTag represents a GriefPrevention claim.
    //
    // For format info, see <@link language gpclaim@>
    //
    // -->

    // <--[language]
    // @name gpclaim@
    // @group Depenizen Object Fetcher Types
    // @plugin Depenizen, GriefPrevention
    // @description
    // gpclaim@ refers to the 'object identifier' of a GriefPreventionClaimTag. The 'gpclaim@' is notation for Denizen's Object
    // Fetcher. The constructor for a GriefPreventionClaimTag is <claim_id>
    // For example, 'gpclaim@1234'.
    //
    // For general info, see <@link language GriefPreventionClaimTag>
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
            claimID = Long.valueOf(id);
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
        // @attribute <GriefPreventionClaimTag.id>
        // @returns ElementTag(Number)
        // @description
        // Returns the GriefPreventionClaim's ID.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("id")) {
            return new ElementTag(claim.getID()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.managers>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns the GriefPreventionClaim's managers.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("managers")) {
            ListTag managers = new ListTag();
            for (String manager : claim.managers) {
                managers.add(new PlayerTag(UUID.fromString(manager)).identify());
            }
            return managers.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.trusted>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns the GriefPreventionClaim's trusted.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("trusted")) {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.add(new PlayerTag(UUID.fromString(trust)).identify());
            }
            return trusted.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.builders>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns the GriefPreventionClaim's builders.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("builders")) {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.add(new PlayerTag(UUID.fromString(trust)).identify());
            }
            return trusted.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.containers>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns the GriefPreventionClaim's containers.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("containers")) {
            ListTag trusted = new ListTag();
            ArrayList<String> c = new ArrayList<>();
            claim.getPermissions(new ArrayList<>(), c, new ArrayList<>(), new ArrayList<>());
            for (String container : c) {
                trusted.add(new PlayerTag(UUID.fromString(container)).identify());
            }
            return trusted.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.accessors>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns the GriefPreventionClaim's accessors.
        // @Plugin Depenizen, GriefPrevention
        // -->
        if (attribute.startsWith("accessors")) {
            ListTag trusted = new ListTag();
            ArrayList<String> a = new ArrayList<>();
            claim.getPermissions(new ArrayList<>(), new ArrayList<>(), a, new ArrayList<>());
            for (String access : a) {
                trusted.add(new PlayerTag(UUID.fromString(access)).identify());
            }
            return trusted.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.owner>
        // @returns PlayerTag/Element
        // @description
        // Returns the GriefPreventionClaim's owner.
        // Can be "Admin" or a PlayerTag.
        // @mechanism GriefPreventionClaim.owner
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("owner")) {
            if (claim.isAdminClaim()) {
                return new ElementTag("Admin").getAttribute(attribute.fulfill(1));
            }
            return new PlayerTag(claim.ownerID)
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.cuboid>
        // @returns CuboidTag
        // @description
        // Returns the GriefPreventionClaim's cuboid area.
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("cuboid")) {
            LocationTag lower = new LocationTag(claim.getLesserBoundaryCorner());
            lower.setY(0);
            LocationTag upper = new LocationTag(claim.getGreaterBoundaryCorner());
            upper.setY(255);
            return new CuboidTag(lower, upper).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_adminclaim>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether GriefPreventionClaim is an Admin Claim.
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("is_adminclaim") || attribute.startsWith("is_admin_claim")) {
            return new ElementTag(claim.isAdminClaim()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.chunks>
        // @returns ListTag(ChunkTag)
        // @description
        // Returns a list of all chunks in the GriefPreventionClaim.
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("chunks")) {
            ListTag chunks = new ListTag();
            for (Chunk chunk : claim.getChunks()) {
                chunks.add(new ChunkTag(chunk).identify());
            }
            return chunks.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.can_siege[<player>]>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the GriefPreventionClaim can siege the player.
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("can_siege") && attribute.hasContext(1)) {
            PlayerTag defender = PlayerTag.valueOf(attribute.getContext(1));
            if (defender == null || defender.getPlayerEntity() == null) {
                return null;
            }
            return new ElementTag(claim.canSiege(defender.getPlayerEntity())).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_sieged>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the GriefPreventionClaim is currently under siege.
        // @Plugin Depenizen, GriefPrevention
        // -->
        else if (attribute.startsWith("is_sieged")) {
            return new ElementTag(claim.siegeData != null).getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // <--[mechanism]
        // @object GriefPreventionClaim
        // @name owner
        // @input PlayerTag/Element
        // @description
        // Sets the owner of the GriefPreventionClaim.
        // Accepts PlayerTag or "admin" to set as admin claim.
        // @tags
        // <GriefPreventionClaimTag.owner>
        // -->
        if (mechanism.matches("owner")) {
            try {
                if (PlayerTag.matches(mechanism.getValue().asString())) {
                    PlayerTag player = PlayerTag.valueOf(mechanism.getValue().asString());
                    dataStore.changeClaimOwner(claim, player.getOfflinePlayer().getUniqueId());
                }
                else if (CoreUtilities.toLowerCase(mechanism.getValue().asString()).equals("admin")) {
                    dataStore.changeClaimOwner(claim, null);
                }
            }
            catch (Exception e) {
                Debug.echoError("Unable to transfer ownership of claim: " + this.identify() + ".");
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
        Debug.echoError("Cannot apply Properties to a GriefPreventionClaim!");
    }
}
