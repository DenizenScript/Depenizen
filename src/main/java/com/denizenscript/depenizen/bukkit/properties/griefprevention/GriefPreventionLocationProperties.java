package com.denizenscript.depenizen.bukkit.properties.griefprevention;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaimTag;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class GriefPreventionLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "GriefPreventionLocation";
    }

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static GriefPreventionLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "griefprevention"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public GriefPreventionLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

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
            // @attribute <LocationTag.griefprevention.has_claim[(<ignore_height>)]>
            // @returns ElementTag(Boolean)
            // @plugin Depenizen, GriefPrevention
            // @description
            // Returns whether there is a GriefPreventionClaim at this location.
            // Optionally specify an ElementTag(Boolean) for whether to ignore Y axis.
            // -->
            if (attribute.startsWith("has_claim")) {
                ElementTag ignoreHeight = new ElementTag(false);
                if (attribute.hasParam()) {
                    ignoreHeight = new ElementTag(attribute.getParam());
                    if (!ignoreHeight.asString().isEmpty() || !ignoreHeight.isBoolean()) {
                        ignoreHeight = new ElementTag(true);
                    }
                }
                return new ElementTag(dataStore.getClaimAt(location, ignoreHeight.asBoolean(), null) != null).getObjectAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <LocationTag.griefprevention.claim>
            // @returns GriefPreventionClaimTag
            // @plugin Depenizen, GriefPrevention
            // @description
            // Returns the GriefPreventionClaim at this location.
            // -->
            else if (attribute.startsWith("claim")) {
                Claim claim = dataStore.getClaimAt(location, false, null);
                if (claim == null) {
                    return null;
                }
                return new GriefPreventionClaimTag(claim).getObjectAttribute(attribute.fulfill(1));
            }

        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
