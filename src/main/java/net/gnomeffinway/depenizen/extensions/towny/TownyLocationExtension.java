package net.gnomeffinway.depenizen.extensions.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
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
        // @attribute <l@location.town>
        // @returns dTown
        // @description
        // Returns the town at the specified location.
        // @plugin Towny
        // -->
        if (attribute.startsWith("town")) {
            try {
                return new dTown(TownyUniverse.getDataSource().getTown(TownyUniverse.getTownName(location)))
                        .getAttribute(attribute.fulfill(1));
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative())
                    dB.echoError(location.identifySimple() + " is not registered to a town!");
            }
        }

        return null;

    }

}
