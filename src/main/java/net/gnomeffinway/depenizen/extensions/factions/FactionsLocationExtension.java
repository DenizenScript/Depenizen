package net.gnomeffinway.depenizen.extensions.factions;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.ps.PS;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dFaction;

public class FactionsLocationExtension extends dObjectExtension {

    public static boolean describes(dObject obj) {
        return obj instanceof dLocation;
    }

    public static FactionsLocationExtension getFrom(dObject obj) {
        if (!describes(obj)) return null;
        else return new FactionsLocationExtension((dLocation) obj);
    }

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
        // @plugin Depenizen, Factions
        // -->
        if (attribute.startsWith("faction"))
            return new dFaction(BoardColl.get().getFactionAt(PS.valueOf(location)))
                    .getAttribute(attribute.fulfill(1));

        return null;

    }

}
