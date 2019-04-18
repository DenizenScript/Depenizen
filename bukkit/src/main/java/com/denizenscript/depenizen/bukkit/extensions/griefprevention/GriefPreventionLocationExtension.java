package com.denizenscript.depenizen.bukkit.extensions.griefprevention;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.griefprevention.GriefPreventionClaim;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class GriefPreventionLocationExtension extends dObjectExtension {

    static DataStore dataStore = GriefPrevention.instance.dataStore;

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static GriefPreventionLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new GriefPreventionLocationExtension((dLocation) object);
        }
    }


    ///////////////////
    // Instance Fields and Methods
    /////////////

    public static final String[] handledTags = new String[] {
            "griefprevention"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private GriefPreventionLocationExtension(dLocation location) {
        this.location = location;
    }

    dLocation location;

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
            // @attribute <l@location.griefprevention.has_claim[(<ignore_height>)]>
            // @returns Element(Boolean)
            // @description
            // Returns whether there is a GriefPreventionClaim at this location.
            // Optionally specify an Element(Boolean) for whether to ignore Y axis.
            // @Plugin DepenizenBukkit, GriefPrevention
            // -->
            if (attribute.startsWith("has_claim")) {
                Element ignoreHeight = new Element(false);
                if (attribute.hasContext(1)) {
                    ignoreHeight = new Element(attribute.getContext(1));
                    if (!ignoreHeight.asString().isEmpty() || !ignoreHeight.isBoolean()) {
                        ignoreHeight = new Element(true);
                    }
                }
                return new Element(dataStore.getClaimAt(location, ignoreHeight.asBoolean(), null) != null).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <l@location.griefprevention.claim>
            // @returns GriefPreventionClaim
            // @description
            // Returns the GriefPreventionClaim at this location.
            // @Plugin DepenizenBukkit, GriefPrevention
            // -->
            else if (attribute.startsWith("claim")) {
                Claim claim = dataStore.getClaimAt(location, false, null);
                if (claim == null) {
                    return null;
                }
                return new GriefPreventionClaim(claim).getAttribute(attribute.fulfill(1));
            }

        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
    }
}
