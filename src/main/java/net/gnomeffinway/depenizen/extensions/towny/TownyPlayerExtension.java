package net.gnomeffinway.depenizen.extensions.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;

public class TownyPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static TownyPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) return null;
        else return new TownyPlayerExtension((dPlayer) pl);
    }

    private TownyPlayerExtension(dPlayer pl) { player = pl; }

    dPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <p@player.town>
        // @returns dTown
        // @description
        // Returns the player's town.
        // @plugin Towny
        // -->
        if (attribute.startsWith("town")) {
            try {
                return new dTown(TownyUniverse.getDataSource().getResident(player.getName()).getTown())
                        .getAttribute(attribute.fulfill(1));
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative())
                    dB.echoError(player.getName() + " is not registered to a town!");
            }
        }

        // <--[tag]
        // @attribute <p@player.nation>
        // @returns dNation
        // @description
        // Returns the player's nation.
        // @plugin Towny
        // -->
        if (attribute.startsWith("nation")) {
            try {
                return new dNation(TownyUniverse.getDataSource().getResident(player.getName()).getTown().getNation())
                        .getAttribute(attribute.fulfill(1));
            }
            catch (NotRegisteredException ex) {
                if (!attribute.hasAlternative())
                    dB.echoError(player.getName() + " is not registered to a nation!");
            }
        }

        return null;

    }

}
