package net.gnomeffinway.depenizen.support.plugins;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.WorldCoord;
import net.aufdemrand.denizen.objects.dCuboid;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.events.Towny.PlayerEntersTownScriptEvent;
import net.gnomeffinway.depenizen.events.Towny.PlayerExitsTownScriptEvent;
import net.gnomeffinway.depenizen.extensions.towny.TownyCuboidExtension;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;
import net.gnomeffinway.depenizen.support.Support;
import net.gnomeffinway.depenizen.extensions.towny.TownyLocationExtension;
import net.gnomeffinway.depenizen.extensions.towny.TownyPlayerExtension;
import org.bukkit.Location;

public class TownySupport extends Support {

    public TownySupport() {
        registerObjects(dTown.class, dNation.class);
        registerProperty(TownyPlayerExtension.class, dPlayer.class);
        registerProperty(TownyLocationExtension.class, dLocation.class);
        registerProperty(TownyCuboidExtension.class, dCuboid.class);
        registerScriptEvents(new PlayerEntersTownScriptEvent());
        registerScriptEvents(new PlayerExitsTownScriptEvent());
        registerAdditionalTags("town", "nation");
    }

    public static Town fromWorldCoord(WorldCoord coord) {
        if (coord == null) {
            return null;
        }
        Location loc = new Location(coord.getBukkitWorld(), coord.getX(), 0, coord.getZ());
        try {
            String name = TownyUniverse.getTownName(loc);
            if (name == null) {
                return null;
            }
            return TownyUniverse.getDataSource().getTown(name);
        }
        catch (NotRegisteredException e) {
            return null;
        }
    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("town")) {

            dTown town = null;

            if (attribute.hasContext(1)) {
                if (dTown.matches(attribute.getContext(1))) {
                    town = dTown.valueOf(attribute.getContext(1));
                } else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid town!");
                    return null;
                }
            }

            if (town == null) {
                dB.echoError("Invalid or missing town!");
                return null;
            }

            return town.getAttribute(attribute.fulfill(1));

        }

        else if (attribute.startsWith("nation")) {

            dNation nation = null;

            if (attribute.hasContext(1)) {
                if (dNation.matches(attribute.getContext(1))) {
                    nation = dNation.valueOf(attribute.getContext(1));
                } else {
                    dB.echoError("Could not match '" + attribute.getContext(1) + "' to a valid nation!");
                    return null;
                }
            }

            if (nation == null) {
                dB.echoError("Invalid or missing nation!");
                return null;
            }

            return nation.getAttribute(attribute.fulfill(1));

        }

        return null;

    }
}
