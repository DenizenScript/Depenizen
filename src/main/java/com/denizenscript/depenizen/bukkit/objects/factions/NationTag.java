package com.denizenscript.depenizen.bukkit.objects.factions;

import com.denizenscript.depenizen.bukkit.objects.towny.TownTag;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;

public class NationTag implements ObjectTag {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static NationTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("nation")
    public static NationTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match nation name

        string = string.replace("nation@", "");
        try {
            return new NationTag(TownyUniverse.getDataSource().getNation(string));
        }
        catch (NotRegisteredException e) {
            return null;
        }
    }

    public static boolean matches(String arg) {
        arg = arg.replace("nation@", "");
        return TownyUniverse.getDataSource().hasNation(arg);
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Nation nation = null;

    public NationTag(Nation nation) {
        if (nation != null) {
            this.nation = nation;
        }
        else {
            Debug.echoError("Nation referenced is null!");
        }
    }

    /////////////////////
    //   ObjectTag Methods
    /////////////////

    private String prefix = "Nation";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public NationTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Nation";
    }

    @Override
    public String identify() {
        return "nation@" + nation.getName();
    }

    @Override
    public String identifySimple() {
        // TODO: Properties?
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <NationTag.allies>
        // @returns ListTag(dNation)
        // @description
        // Returns a list of the nation's allies.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("allies")) {
            ListTag list = new ListTag();
            for (Nation ally : nation.getAllies()) {
                list.add(new NationTag(ally).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.assistants>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of the nation's assistants.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("assistants")) {
            ListTag list = new ListTag();
            for (Resident resident : nation.getAssistants()) {
                list.add(PlayerTag.valueOf(resident.getName()).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.balance>
        // @returns ElementTag(Decimal)
        // @description
        // Returns the current money balance of the nation.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("balance")) {
            try {
                return new ElementTag(nation.getHoldingBalance()).getAttribute(attribute.fulfill(1));
            }
            catch (EconomyException e) {
                if (!attribute.hasAlternative()) {
                    Debug.echoError("Invalid economy response!");
                }
            }
        }

        // <--[tag]
        // @attribute <NationTag.capital>
        // @returns TownTag
        // @description
        // Returns the capital city of the nation as a dTown.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("capital")) {
            if (nation.hasCapital()) {
                return new TownTag(nation.getCapital())
                        .getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <NationTag.enemies>
        // @returns ListTag(dNation)
        // @description
        // Returns a list of the nation's enemies.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("enemies")) {
            ListTag list = new ListTag();
            for (Nation enemy : nation.getEnemies()) {
                list.add(new NationTag(enemy).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.is_neutral>
        // @returns ElementTag(Boolean)
        // @description
        // Returns true if the nation is neutral.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("isneutral") || attribute.startsWith("is_neutral")) {
            return new ElementTag(nation.isNeutral())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.king>
        // @returns PlayerTag
        // @description
        // Returns the king of the nation.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("king")) {
            return PlayerTag.valueOf(nation.getCapital().getMayor().getName())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.name>
        // @returns ElementTag
        // @description
        // Returns the nation's name.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("name")) {
            return new ElementTag(nation.getName())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.player_count>
        // @returns ElementTag(Number)
        // @description
        // Returns the amount of players in the nation.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
            return new ElementTag(nation.getNumResidents())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.relation[<nation>]>
        // @returns ElementTag
        // @description
        // Returns the nation's current relation with another nation.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("relation")) {

            try {
                NationTag to = valueOf(attribute.getContext(1));

                if (nation.hasAlly(to.nation)) {
                    return new ElementTag("allies").getAttribute(attribute.fulfill(1));
                }
                else if (nation.hasEnemy(to.nation)) {
                    return new ElementTag("enemies").getAttribute(attribute.fulfill(1));
                }
                else {
                    return new ElementTag("neutral").getAttribute(attribute.fulfill(1));
                }

            }
            catch (Exception e) {
            }

        }

        // <--[tag]
        // @attribute <NationTag.residents>
        // @returns ListTag(PlayerTag)
        // @description
        // Returns a list of the nation's residents.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("residents")) {
            ListTag list = new ListTag();
            for (Resident resident : nation.getResidents()) {
                list.add(PlayerTag.valueOf(resident.getName()).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.tag>
        // @returns ElementTag
        // @description
        // Returns the nation's tag.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("tag")) {
            if (nation.hasTag()) {
                return new ElementTag(nation.getTag())
                        .getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <NationTag.taxes>
        // @returns ElementTag(Decimal)
        // @description
        // Returns the nation's current taxes.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("taxes")) {
            return new ElementTag(nation.getTaxes())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.town_count>
        // @returns ElementTag(Number)
        // @description
        // Returns the number of towns in the nation.
        // @Plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("towncount") || attribute.startsWith("town_count")) {
            return new ElementTag(nation.getNumTowns())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <NationTag.type>
        // @returns ElementTag
        // @description
        // Always returns 'Nation' for dNation objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("type")) {
            return new ElementTag("Nation").getAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getAttribute(attribute);

    }
}
