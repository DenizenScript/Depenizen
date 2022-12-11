package com.denizenscript.depenizen.bukkit.objects.griefprevention;

import com.denizenscript.denizen.objects.*;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.denizencore.flags.AbstractFlagTracker;
import com.denizenscript.denizencore.flags.FlaggableObject;
import com.denizenscript.denizencore.flags.RedirectionFlagTracker;
import com.denizenscript.denizencore.objects.*;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.ObjectTagProcessor;
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

public class GriefPreventionClaimTag implements ObjectTag, Adjustable, FlaggableObject {

    // <--[ObjectType]
    // @name GriefPreventionClaimTag
    // @prefix gpclaim
    // @base ElementTag
    // @implements FlaggableObject
    // @format
    // The identity format for claims is <claim_id>
    // For example, 'gpclaim@1234'.
    //
    // @plugin Depenizen, GriefPrevention
    // @description
    // A GriefPreventionClaimTag represents a GriefPrevention claim.
    //
    // This object type is flaggable.
    // Flags on this object type will be stored in the server saves file, under special sub-key "__depenizen_gp_claims_id"
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
    public String identify() {
        return "gpclaim@" + claim.getID();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public AbstractFlagTracker getFlagTracker() {
        return new RedirectionFlagTracker(DenizenCore.serverFlagMap, "__depenizen_gp_claims_id." + claim.getID());
    }

    @Override
    public void reapplyTracker(AbstractFlagTracker tracker) {
        // Nothing to do.
    }

    public String simple() {
        return String.valueOf(claim.getID());
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    public static ObjectTagProcessor<GriefPreventionClaimTag> tagProcessor = new ObjectTagProcessor<>();

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        return tagProcessor.getObjectAttribute(this, attribute);
    }

    public static void register() {

        AbstractFlagTracker.registerFlagHandlers(tagProcessor);

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.id>
        // @returns ElementTag(Number)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's ID.
        // -->
        tagProcessor.registerTag(ElementTag.class, "id", (attribute, object) -> {
            return new ElementTag(object.claim.getID());
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.managers>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's managers.
        // -->
        tagProcessor.registerTag(ListTag.class, "managers", (attribute, object) -> {
            ListTag managers = new ListTag();
            for (String manager : object.claim.managers) {
                managers.addObject(new PlayerTag(UUID.fromString(manager)));
            }
            return managers;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.trusted>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's trusted.
        // -->
        tagProcessor.registerTag(ListTag.class, "trusted", (attribute, object) -> {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            object.claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.addObject(new PlayerTag(UUID.fromString(trust)));
            }
            return trusted;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.builders>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's builders.
        // -->
        tagProcessor.registerTag(ListTag.class, "builders", (attribute, object) -> {
            ListTag trusted = new ListTag();
            ArrayList<String> b = new ArrayList<>();
            object.claim.getPermissions(b, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            for (String trust : b) {
                trusted.addObject(new PlayerTag(UUID.fromString(trust)));
            }
            return trusted;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.containers>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's containers.
        // -->
        tagProcessor.registerTag(ListTag.class, "containers", (attribute, object) -> {
            ListTag trusted = new ListTag();
            ArrayList<String> c = new ArrayList<>();
            object.claim.getPermissions(new ArrayList<>(), c, new ArrayList<>(), new ArrayList<>());
            for (String container : c) {
                trusted.addObject(new PlayerTag(UUID.fromString(container)));
            }
            return trusted;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.accessors>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's accessors.
        // -->
        tagProcessor.registerTag(ListTag.class, "accessors", (attribute, object) -> {
            ListTag trusted = new ListTag();
            ArrayList<String> a = new ArrayList<>();
            object.claim.getPermissions(new ArrayList<>(), new ArrayList<>(), a, new ArrayList<>());
            for (String access : a) {
                trusted.addObject(new PlayerTag(UUID.fromString(access)));
            }
            return trusted;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.owner>
        // @returns ObjectTag
        // @mechanism GriefPreventionClaimTag.owner
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's owner.
        // Can be "Admin" or a PlayerTag.
        // -->
        tagProcessor.registerTag(ObjectTag.class, "owner", (attribute, object) -> {
            if (object.claim.isAdminClaim()) {
                return new ElementTag("Admin");
            }
            return new PlayerTag(object.claim.ownerID);
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.cuboid>
        // @returns CuboidTag
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the GriefPreventionClaim's cuboid area.
        // -->
        tagProcessor.registerTag(CuboidTag.class, "cuboid", (attribute, object) -> {
            LocationTag lower = new LocationTag(object.claim.getLesserBoundaryCorner());
            LocationTag upper = new LocationTag(object.claim.getGreaterBoundaryCorner());
            upper.setY(upper.getWorld().getMaxHeight());
            return new CuboidTag(lower, upper);
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.world>
        // @returns WorldTag
        // @Plugin Depenizen, GriefPrevention
        // @description
        // Returns the world this GriefPreventionClaim is in.
        // -->
        tagProcessor.registerTag(WorldTag.class, "world", (attribute, object) -> {
            return new WorldTag(object.claim.getLesserBoundaryCorner().getWorld());
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_adminclaim>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether GriefPreventionClaim is an Admin Claim.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_adminclaim", (attribute, object) -> {
            return new ElementTag(object.claim.isAdminClaim());
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.chunks>
        // @returns ListTag(ChunkTag)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns a list of all chunks in the GriefPreventionClaim.
        // -->
        tagProcessor.registerTag(ListTag.class, "chunks", (attribute, object) -> {
            ListTag chunks = new ListTag();
            for (Chunk chunk : object.claim.getChunks()) {
                chunks.addObject(new ChunkTag(chunk));
            }
            return chunks;
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.can_siege[<player>]>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether the GriefPreventionClaim can siege the player.
        // -->
        tagProcessor.registerTag(ElementTag.class, "can_siege", (attribute, object) -> {
            if (!attribute.hasParam()) {
                return null;
            }
            PlayerTag defender = attribute.paramAsType(PlayerTag.class);
            if (defender == null || defender.getPlayerEntity() == null) {
                return null;
            }
            return new ElementTag(object.claim.canSiege(defender.getPlayerEntity()));
        });

        // <--[tag]
        // @attribute <GriefPreventionClaimTag.is_sieged>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, GriefPrevention
        // @description
        // Returns whether the GriefPreventionClaim is currently under siege.
        // -->
        tagProcessor.registerTag(ElementTag.class, "is_sieged", (attribute, object) -> {
            return new ElementTag(object.claim.siegeData != null);
        });
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
