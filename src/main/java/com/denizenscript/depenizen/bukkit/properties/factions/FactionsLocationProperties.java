package com.denizenscript.depenizen.bukkit.properties.factions;

import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.dFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

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

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static FactionsLocationProperties getFrom(dObject object) {
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
