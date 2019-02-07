package com.denizenscript.depenizen.bukkit.extensions.factions;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class FactionsLocationExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }

    public static FactionsLocationExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new FactionsLocationExtension((dLocation) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "faction"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private FactionsLocationExtension(dLocation location) {
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
        // @Plugin DepenizenBukkit, Factions
        // -->
        if (attribute.startsWith("faction")) {
            return new dFaction(BoardColl.get().getFactionAt(PS.valueOf(location)))
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
