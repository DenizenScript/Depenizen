package com.denizenscript.depenizen.bukkit.properties.towny;

import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.depenizen.bukkit.objects.towny.NationTag;
import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;

public class TownyPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "TownyPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static TownyPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new TownyPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "has_nation", "has_town", "mode_list", "nation_ranks", "nation", "town_ranks", "town"
    };

    public static final String[] handledMechs = new String[] {
    }; // None

    public TownyPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    public Resident getResident() {
        return TownyUniverse.getInstance().getResident(player.getUUID());
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <PlayerTag.has_nation>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the player is part of a nation.
        // -->
        if (attribute.startsWith("has_nation")) {
            return new ElementTag(getResident().hasNation()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.has_town>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, Towny
        // @description
        // Returns whether the player is part of a town.
        // -->
        if (attribute.startsWith("has_town")) {
            return new ElementTag(getResident().hasTown()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.mode_list>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the player's towny modes as a list.
        // -->
        else if (attribute.startsWith("mode_list")) {
            ListTag modes = new ListTag();
            for (String mode : getResident().getModes()) {
                modes.addObject(new ElementTag(mode));
            }
            return modes.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.nation_ranks>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the player's nation ranks.
        // -->
        else if (attribute.startsWith("nation_ranks")) {
            ListTag ranks = new ListTag();
            for (String rank : getResident().getNationRanks()) {
                ranks.addObject(new ElementTag(rank));
            }
            return ranks.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.nation>
        // @returns NationTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the player's nation.
        // -->
        if (attribute.startsWith("nation")) {
            try {
                if (getResident().hasNation()) {
                    return new NationTag(getResident().getTown().getNation()).getObjectAttribute(attribute.fulfill(1));
                }
                else {
                    return null;
                }
            }
            catch (NotRegisteredException e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("'" + player.getName() + "' is not registered to a nation in Towny!");
                }
            }
        }

        // <--[tag]
        // @attribute <PlayerTag.town_ranks>
        // @returns ListTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the player's town ranks.
        // -->
        else if (attribute.startsWith("town_ranks")) {
            ListTag ranks = new ListTag();
            for (String rank : getResident().getTownRanks()) {
                ranks.addObject(new ElementTag(rank));
            }
            return ranks.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PlayerTag.town>
        // @returns TownTag
        // @plugin Depenizen, Towny
        // @description
        // Returns the player's town.
        // -->
        if (attribute.startsWith("town")) {
            try {
                if (getResident().hasTown()) {
                    return new TownTag(getResident().getTown()).getObjectAttribute(attribute.fulfill(1));
                }
                else {
                    return null;
                }
            }
            catch (NotRegisteredException e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("'" + player.getName() + "' is not registered to a town in Towny!");
                }
            }
        }

        return null;

    }
}
