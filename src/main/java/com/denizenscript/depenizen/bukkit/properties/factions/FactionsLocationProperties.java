package com.denizenscript.depenizen.bukkit.properties.factions;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.factions.dFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import com.denizenscript.denizen.objects.dLocation;
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
        return object instanceof dLocation;
    }

    public static FactionsLocationProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsLocationProperties((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private FactionsLocationProperties(dLocation location) {
        this.location = location;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.faction>
        // @returns dFaction
        // @description
        // Returns the faction at the location. (Can also be SafeZone, WarZone, or Wilderness)
        // @Plugin Depenizen, Factions
        // -->
        if (attribute.startsWith("faction")) {
            return new dFaction(BoardColl.get().getFactionAt(PS.valueOf(location)))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }
}
