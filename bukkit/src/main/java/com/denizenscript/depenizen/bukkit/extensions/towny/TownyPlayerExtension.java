package com.denizenscript.depenizen.bukkit.extensions.towny;

import com.denizenscript.depenizen.bukkit.extensions.dObjectExtension;
import com.denizenscript.depenizen.bukkit.objects.dNation;
import com.denizenscript.depenizen.bukkit.objects.dTown;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

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

    public static final String[] handledTags = new String[] {
            "has_nation", "has_town", "mode_list", "nation_ranks", "nation", "town_ranks", "town"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    private TownyPlayerExtension(dPlayer player) {
        this.player = player;
    }

    dPlayer player = null;

    public Resident getResident() throws NotRegisteredException {
        return TownyUniverse.getDataSource().getResident(player.getName());
    }

    @Override
    public String getAttribute(Attribute attribute) {

        try {
            // <--[tag]
            // @attribute <p@player.has_nation>
            // @returns Element(Boolean)
            // @description
            // Returns whether the player is part of a nation.
            // @Plugin DepenizenBukkit, Towny
            // -->
            if (attribute.startsWith("has_nation")) {
                return new Element(getResident().hasNation()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.has_town>
            // @returns Element(Boolean)
            // @description
            // Returns whether the player is part of a town.
            // @Plugin DepenizenBukkit, Towny
            // -->
            if (attribute.startsWith("has_town")) {
                return new Element(getResident().hasTown()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.mode_list>
            // @returns dList(Element)
            // @description
            // Returns the player's towny modes as a list.
            // @Plugin DepenizenBukkit, Towny
            // -->
            else if (attribute.startsWith("mode_list")) {
                dList modes = new dList();
                for (String mode : getResident().getModes()) {
                    modes.add(new Element(mode).identify());
                }
                return modes.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.nation_ranks>
            // @returns dList(Element)
            // @description
            // Returns the player's nation ranks.
            // @Plugin DepenizenBukkit, Towny
            // -->
            else if (attribute.startsWith("nation_ranks")) {
                dList ranks = new dList();
                for (String rank : getResident().getNationRanks()) {
                    ranks.add(new Element(rank).identify());
                }
                return ranks.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.nation>
            // @returns dNation
            // @description
            // Returns the player's nation.
            // @Plugin DepenizenBukkit, Towny
            // -->
            if (attribute.startsWith("nation")) {
                try {
                    if (getResident().hasNation()) {
                        return new dNation(getResident().getTown().getNation()).getAttribute(attribute.fulfill(1));
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
            // @attribute <p@player.town_ranks>
            // @returns dList(Element)
            // @description
            // Returns the player's town ranks.
            // @Plugin DepenizenBukkit, Towny
            // -->
            else if (attribute.startsWith("town_ranks")) {
                dList ranks = new dList();
                for (String rank : getResident().getTownRanks()) {
                    ranks.add(new Element(rank).identify());
                }
                return ranks.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.town>
            // @returns dTown
            // @description
            // Returns the player's town.
            // @Plugin DepenizenBukkit, Towny
            // -->
            if (attribute.startsWith("town")) {
                try {
                    if (getResident().hasTown()) {
                        return new dTown(getResident().getTown()).getAttribute(attribute.fulfill(1));
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
        }
        catch (NotRegisteredException e) {
            if (!attribute.hasAlternative()) {
                dB.echoError("'" + player.getName() + "' is not registered in Towny!");
            }
        }

        return null;

    }

}
