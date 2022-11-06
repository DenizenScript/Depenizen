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
import com.denizenscript.denizencore.utilities.debugging.Debug;
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
            "has_town", "town", "is_wilderness", "is_nationzone", "towny"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyLocationProperties(LocationTag location) {
        this.location = location;
    }

    public LocationTag location;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        if (attribute.startsWith("towny")) {
            attribute = attribute.fulfill(1);
            try {

                // <--[tag]
                // @attribute <LocationTag.towny.is_pvp>
                // @returns ElementTag(Boolean)
                // @plugin Depenizen, Towny
                // @description
                // Returns whether Towny would block PVP here.
                // -->
                if (attribute.startsWith("is_pvp")) {
                    return new ElementTag(TownyAPI.getInstance().isPVP(location)).getObjectAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <LocationTag.towny.resident>
                // @returns PlayerTag
                // @plugin Depenizen, Towny
                // @description
                // Returns the resident of a Towny plot at the location, if any.
                // -->
                if (attribute.startsWith("resident")) {
                    TownBlock block = TownyAPI.getInstance().getTownBlock(location);
                    if (block == null) {
                        return null;
                    }
                    if (!block.hasResident()) {
                        return null;
                    }
                    UUID player = block.getResident().getUUID();
                    if (player == null) {
                        return null;
                    }
                    return new PlayerTag(player).getObjectAttribute(attribute.fulfill(1));
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
        // Can be RESIDENTIAL, COMMERCIAL, ARENA, EMBASSY, WILDS, INN, JAIL, FARM, or BANK.
        // -->
        if (attribute.startsWith("towny_type")) {
            TownBlock block = TownyAPI.getInstance().getTownBlock(location);
            if (block != null) {
                return new ElementTag(block.getType().getName()).getObjectAttribute(attribute.fulfill(1));
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
            if (TownyAPI.getInstance().getTown(location) != null) {
                return new ElementTag(true).getObjectAttribute(attribute.fulfill(1));
            }
            else {
                return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
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
            String town = TownyAPI.getInstance().getTown(location);
            if (town == null) {
                return null;
            }
            return new TownTag(TownyUniverse.getInstance().getTown(town))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LocationTag.is_nationzone>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is a nation zone.
        // -->
        if (attribute.startsWith("is_nationzone")) {
            return new ElementTag(TownyAPI.getInstance().isNationZone(location)).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <LocationTag.is_wilderness>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is wilderness.
        // -->
        if (attribute.startsWith("is_wilderness")) {
            return new ElementTag(TownyAPI.getInstance().isWilderness(location)).getObjectAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
