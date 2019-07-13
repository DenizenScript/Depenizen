package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.towny.dTown;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.denizenscript.denizen.objects.dLocation;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class TownyLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TownyLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof dLocation;
    }

    public static TownyLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyLocationProperties((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_town", "town", "is_wilderness"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyLocationProperties(dLocation location) {
        this.location = location;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.has_town>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the location is within a town.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("has_town")) {
            if (TownyUniverse.getTownName(location) != null) {
                return new ElementTag(true).getAttribute(attribute.fulfill(1));
            }
            else {
                return new ElementTag(false).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <l@location.town>
        // @returns dTown
        // @description
        // Returns the town at the specified location.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("town")) {
            try {
                String town = TownyUniverse.getTownName(location);
                if (town == null) {
                    return null;
                }
                return new dTown(TownyUniverse.getDataSource().getTown(town))
                        .getAttribute(attribute.fulfill(1));
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError(location.identifySimple() + " is not registered to a town!");
                }
            }
        }

        // <--[tag]
        // @attribute <l@location.is_wilderness>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the location is wilderness.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("is_wilderness")) {
            return new ElementTag(TownyUniverse.isWilderness(location.getBlock())).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
