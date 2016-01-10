package net.gnomeffinway.depenizen.extensions.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dTown;

public class TownyLocationExtension extends dObjectExtension {

    public static boolean describes(dObject loc) {
        return loc instanceof dLocation;
    }

    public static TownyLocationExtension getFrom(dObject loc) {
        if (!describes(loc)) return null;
        else return new TownyLocationExtension((dLocation) loc);
    }

    private TownyLocationExtension(dLocation loc) {
        location = loc;
    }

    dLocation location = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <l@location.has_town>
        // @returns Element(Boolean)
        // @description
        // Returns whether the location is within a town.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("has_town")) {
            if (TownyUniverse.getTownName(location) != null)
                return Element.TRUE.getAttribute(attribute.fulfill(1));
            else
                return Element.FALSE.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <l@location.town>
        // @returns dTown
        // @description
        // Returns the town at the specified location.
        // @plugin Depenizen, Towny
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
                if (!attribute.hasAlternative())
                    dB.echoError(location.identifySimple() + " is not registered to a town!");
            }
        }

        // <--[tag]
        // @attribute <l@location.is_wilderness>
        // @returns Element(Boolean)
        // @description
        // Returns whether the location is wilderness.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("is_wilderness")) {
            return new Element(TownyUniverse.isWilderness(location.getBlock())).getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
