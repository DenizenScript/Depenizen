package com.denizenscript.depenizen.bukkit.objects;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dNation implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    public static dNation valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("nation")
    public static dNation valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match town name

        string = string.replace("nation@", "");
        try {
            return new dNation(TownyUniverse.getDataSource().getNation(string));
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

    public dNation(Nation nation) {
        if (nation != null) {
            this.nation = nation;
        }
        else {
            dB.echoError("Nation referenced is null!");
        }
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Nation";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dNation setPrefix(String prefix) {
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
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <nation@nation.assistants>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of the nation's assistants.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("assistants")) {
            dList list = new dList();
            for (Resident resident : nation.getAssistants()) {
                list.add(dPlayer.valueOf(resident.getName()).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.balance>
        // @returns Element(Decimal)
        // @description
        // Returns the current money balance of the nation.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("balance")) {
            try {
                return new Element(nation.getHoldingBalance()).getAttribute(attribute.fulfill(1));
            }
            catch (EconomyException e) {
                if (!attribute.hasAlternative()) {
                    dB.echoError("Invalid economy response!");
                }
            }
        }

        // <--[tag]
        // @attribute <nation@nation.capital>
        // @returns dTown
        // @description
        // Returns the capital city of the nation as a dTown.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("capital")) {
            if (nation.hasCapital()) {
                return new dTown(nation.getCapital())
                        .getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <nation@nation.is_neutral>
        // @returns Element(Boolean)
        // @description
        // Returns true if the nation is neutral.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("isneutral") || attribute.startsWith("is_neutral")) {
            return new Element(nation.isNeutral())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.king>
        // @returns dPlayer
        // @description
        // Returns the king of the nation.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("king")) {
            return dPlayer.valueOf(nation.getCapital().getMayor().getName())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.name>
        // @returns Element
        // @description
        // Returns the nation's name.
        // @plugin Depenizen, Towny
        else if (attribute.startsWith("name")) {
            return new Element(nation.getName())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.player_count>
        // @returns Element(Number)
        // @description
        // Returns the amount of players in the nation.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count")) {
            return new Element(nation.getNumResidents())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.relation[<nation>]>
        // @returns Element
        // @description
        // Returns the nation's current relation with another nation.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("relation")) {

            try {
                dNation to = valueOf(attribute.getContext(1));

                if (nation.hasAlly(to.nation)) {
                    return new Element("allies").getAttribute(attribute.fulfill(1));
                }
                else if (nation.hasEnemy(to.nation)) {
                    return new Element("enemies").getAttribute(attribute.fulfill(1));
                }
                else {
                    return new Element("neutral").getAttribute(attribute.fulfill(1));
                }

            }
            catch (Exception e) {
            }

        }

        // <--[tag]
        // @attribute <nation@nation.residents>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of the nation's residents.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("residents")) {
            dList list = new dList();
            for (Resident resident : nation.getResidents()) {
                list.add(dPlayer.valueOf(resident.getName()).identify());
            }
            return list.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.tag>
        // @returns Element
        // @description
        // Returns the nation's tag.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("tag")) {
            if (nation.hasTag()) {
                return new Element(nation.getTag())
                        .getAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <nation@nation.taxes>
        // @returns Element(Decimal)
        // @description
        // Returns the nation's current taxes.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("taxes")) {
            return new Element(nation.getTaxes())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.town_count>
        // @returns Element(Number)
        // @description
        // Returns the number of towns in the nation.
        // @plugin Depenizen, Towny
        // -->
        else if (attribute.startsWith("towncount") || attribute.startsWith("town_count")) {
            return new Element(nation.getNumTowns())
                    .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.type>
        // @returns Element
        // @description
        // Always returns 'Nation' for dNation objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, Towny
        // -->
        if (attribute.startsWith("type")) {
            return new Element("Nation").getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);

    }
}
