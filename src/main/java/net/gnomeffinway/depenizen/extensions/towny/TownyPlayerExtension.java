package net.gnomeffinway.depenizen.extensions.towny;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.dNation;
import net.gnomeffinway.depenizen.objects.dTown;

public class TownyPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject object) {
        return object instanceof dPlayer;
    }

    public static TownyPlayerExtension getFrom(dObject object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyPlayerExtension((dPlayer) object);
        }
    }

    private TownyPlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

    @Override
    public String getAttribute(Attribute attribute) {

        Resident resident;
        try {
            resident = TownyUniverse.getDataSource().getResident(player.getName());
        }
        catch (NotRegisteredException e) {
            if (!attribute.hasAlternative()) {
                dB.echoError("'" + player.getName() + "' is not registered in Towny!");
            }
            return null;
        }

        // <--[tag]
        // @attribute <p@player.has_nation>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is part of a nation.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("has_nation")) {
            return new Element(resident.hasNation()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.has_town>
        // @returns Element(Boolean)
        // @description
        // Returns whether the player is part of a town.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("has_town")) {
            return new Element(resident.hasTown()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <p@player.nation>
        // @returns dNation
        // @description
        // Returns the player's nation.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("nation")) {
            try {
                if (resident.hasNation()) {
                    return new dNation(resident.getTown().getNation()).getAttribute(attribute.fulfill(1));
                }
                else {
                    return null;
                }
            }
            catch (NotRegisteredException e) {
                if (!attribute.hasAlternative()) {
                    dB.echoError("'" + player.getName() + "' is not registered to a nation in Towny!");
                }
            }
        }

        // <--[tag]
        // @attribute <p@player.town>
        // @returns dTown
        // @description
        // Returns the player's town.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("town")) {
            try {
                if (resident.hasTown()) {
                    return new dTown(resident.getTown()).getAttribute(attribute.fulfill(1));
                }
                else {
                    return null;
                }
            }
            catch (NotRegisteredException e) {
                if (!attribute.hasAlternative()) {
                    dB.echoError("'" + player.getName() + "' is not registered to a town in Towny!");
                }
            }
        }

        // <--[tag]
        // @attribute <p@player.mode_list>
        // @returns dList(Element)
        // @description
        // Returns the player's towny modes as a list.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("mode_list")) {
            dList modes = new dList();
            for (String mode : resident.getModes()) {
                modes.add(new Element(mode).identify());
            }
            return modes.getAttribute(attribute.fulfill(1));
        }

        return null;

    }

}
