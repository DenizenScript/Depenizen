package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.factions.FactionTag;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class FactionsLocationProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "FactionsLocation";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }

    public static FactionsLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsLocationProperties((LocationTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public FactionsLocationProperties(LocationTag location) {
        this.location = location;
    }

    LocationTag location;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <LocationTag.faction>
        // @returns FactionTag
        // @plugin Depenizen, Factions
        // @description
        // Returns the faction at the location. (Can also be SafeZone, WarZone, or Wilderness)
        // -->
        if (attribute.startsWith("faction")) {
            return new FactionTag(BoardColl.get().getFactionAt(PS.valueOf(location)))
                    .getObjectAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
