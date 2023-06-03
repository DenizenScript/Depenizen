package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;

import java.util.UUID;

public class TownyLocationProperties {

    public static void register() {

        // <--[tag]
        // @attribute <LocationTag.towny_allows_pvp>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether Towny would allow PVP here.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "towny_allows_pvp", (attribute, location) -> {
            return new ElementTag(TownyAPI.getInstance().isPVP(location));
        });

        // <--[tag]
        // @attribute <LocationTag.towny_resident>
        // @returns PlayerTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the resident of a Towny plot at the location, if any.
        // -->
        LocationTag.tagProcessor.registerTag(PlayerTag.class, "towny_resident", (attribute, location) -> {
            try {
                return getResidentAtLocation(location);
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative()) {
                    attribute.echoError("Towny tag NotRegisteredException: " + ex.getMessage());
                }
            }
            return null;
        });

        LocationTag.tagProcessor.registerTag(PlayerTag.class, "towny", (attribute, location) -> {
            try {
                // <--[tag]
                // @attribute <LocationTag.towny.resident>
                // @returns PlayerTag
                // @plugin Depenizen, Towny
                // @deprecated use 'towny_resident'
                // @description
                // Returns the resident of a Towny plot at the location, if any.
                // Deprecated in favor of <@link tag LocationTag.towny_resident>.
                // -->
                if (attribute.startsWith("resident", 2)) {
                    attribute.fulfill(1);
                    return getResidentAtLocation(location);
                }
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative()) {
                    attribute.echoError("Towny tag NotRegisteredException: " + ex.getMessage());
                }
            }
            return null;
        });

        // <--[tag]
        // @attribute <LocationTag.towny_type>
        // @returns ElementTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the type of the Towny area this location is in.
        // Can be Default, Shop, Arena, Embassy, Wilds, Inn, Jail, Farm, Bank
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "towny_type", (attribute, location) -> {
            TownBlock block = TownyAPI.getInstance().getTownBlock(location);
            if (block != null) {
                return new ElementTag(block.getType().getName());
            }
            return null;
        });

        // <--[tag]
        // @attribute <LocationTag.has_town>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is within a town.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "has_town", (attribute, location) -> {
            return new ElementTag(TownyAPI.getInstance().getTown(location) != null);
        });

        // <--[tag]
        // @attribute <LocationTag.town>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the town at the specified location.
        // -->
        LocationTag.tagProcessor.registerTag(TownTag.class, "town", (attribute, location) -> {
            String town = TownyAPI.getInstance().getTownName(location);
            if (town == null) {
                return null;
            }
            return new TownTag(TownyUniverse.getInstance().getTown(town));
        });

        // <--[tag]
        // @attribute <LocationTag.is_nation_zone>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is a nation zone.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "is_nation_zone", (attribute, location) -> {
            return new ElementTag(TownyAPI.getInstance().isNationZone(location));
        });

        // <--[tag]
        // @attribute <LocationTag.is_wilderness>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the location is wilderness.
        // -->
        LocationTag.tagProcessor.registerTag(ElementTag.class, "is_wilderness", (attribute, location) -> {
            return new ElementTag(TownyAPI.getInstance().isWilderness(location));
        });
    }

    public static PlayerTag getResidentAtLocation(LocationTag location) throws NotRegisteredException {
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
        return new PlayerTag(player);
    }
}
