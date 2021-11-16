package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

import java.util.UUID;

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
        return object instanceof LocationTag;
    }

    public static TownyLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_town", "town", "is_wilderness", "towny"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyLocationProperties(LocationTag location) {
        this.location = location;
    }

    public LocationTag location;

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute.startsWith("towny")) {
            attribute = attribute.fulfill(1);
            try {
                TownBlock block = TownyAPI.getInstance().getTownBlock(location);
                if (block == null) {
                    return null;
                }

                // <--[tag]
                // @attribute <LocationTag.towny.resident>
                // @returns PlayerTag
                // @plugin Depenizen, Towny
                // @description
                // Returns the resident of a Towny plot at the location, if any.
                // -->
                if (attribute.startsWith("resident")) {
                    if (!block.hasResident()) {
                        return null;
                    }
                    UUID player = block.getResident().getUUID();
                    if (player == null) {
                        return null;
                    }
                    return new PlayerTag(player).getAttribute(attribute.fulfill(1));
                }
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Towny tag NotRegisteredException: " + ex.getMessage());
                }
            }
        }

        // <--[tag]
        // @attribute <LocationTag.towny_type>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the type of the Towny area this location is in.
        // Can be RESIDENTIAL, COMMERCIAL, ARENA, EMBASSY, WILDS, SPLEEF, INN, JAIL, FARM, or BANK.
        // -->
        if (attribute.startsWith("towny_type")) {
            TownBlock block = TownyAPI.getInstance().getTownBlock(location);
            if (block != null) {
                return new ElementTag(block.getType().name()).getAttribute(attribute.fulfill(1));
            }
            return null;
        }

        // <--[tag]
        // @attribute <LocationTag.has_town>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is within a town.
        // -->
        if (attribute.startsWith("has_town")) {
            if (TownyAPI.getInstance().getTownName(location) != null) {
                return new ElementTag(true).getAttribute(attribute.fulfill(1));
            }
            else {
                return new ElementTag(false).getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <LocationTag.town>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town at the specified location.
        // -->
        if (attribute.startsWith("town")) {
            String town = TownyAPI.getInstance().getTownName(location);
            if (town == null) {
                return null;
            }
            return new TownTag(TownyUniverse.getInstance().getTown(town))
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LocationTag.is_wilderness>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is wilderness.
        // -->
        else if (attribute.startsWith("is_wilderness")) {
            return new ElementTag(TownyAPI.getInstance().isWilderness(location.getBlock())).getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
