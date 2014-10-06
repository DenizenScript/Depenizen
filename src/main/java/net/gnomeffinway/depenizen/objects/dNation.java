package net.gnomeffinway.depenizen.objects;

import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.Fetchable;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dNation implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("nation")
    public static dNation valueOf(String string) {
        if (string == null) return null;

        ////////
        // Match town name

        string = string.replace("nation@", "");
        try {
            return new dNation(TownyUniverse.getDataSource().getNation(string));
        } catch (NotRegisteredException e) {
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
        if (nation != null)
            this.nation = nation;
        else
            dB.echoError("Nation referenced is null!");
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
        // @attribute <nation@nation.balance>
        // @returns Element(Decimal)
        // @description
        // Returns the current money balance of the nation.
        // @plugin Towny
        // -->
        if (attribute.startsWith("balance")) {
            try {
                return new Element(nation.getHoldingBalance()).getAttribute(attribute.fulfill(1));
            } catch (EconomyException e) {
                dB.echoError("Invalid economy response!");
            }
        }

        // <--[tag]
        // @attribute <nation@nation.capital>
        // @returns dTown
        // @description
        // Returns the capital city of the nation as a dTown.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("capital")) {
            if (nation.hasCapital())
                return new dTown(nation.getCapital())
                        .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.is_neutral>
        // @returns Element(Boolean)
        // @description
        // Returns true if the nation is neutral.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("isneutral") || attribute.startsWith("is_neutral"))
            return new Element(nation.isNeutral())
                   .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <nation@nation.king>
        // @returns dPlayer
        // @description
        // Returns the king of the nation.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("king"))
            return dPlayer.valueOf(nation.getCapital().getMayor().getName())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <nation@nation.name>
        // @returns Element
        // @description
        // Returns the nation's name.
        // @plugin Towny
        else if (attribute.startsWith("name"))
            return new Element(nation.getName())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <nation@nation.player_count>
        // @returns Element(Number)
        // @description
        // Returns the amount of players in the nation.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            return new Element(nation.getNumResidents())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <nation@nation.relation[<nation>]>
        // @returns Element
        // @description
        // Returns the nation's current relation with another nation.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("relation")) {

            try {
                dNation to = valueOf(attribute.getContext(1));

                if (nation.hasAlly(to.nation))
                    return new Element("allies").getAttribute(attribute.fulfill(1));
                else if (nation.hasEnemy(to.nation))
                    return new Element("enemies").getAttribute(attribute.fulfill(1));
                else
                    return new Element("neutral").getAttribute(attribute.fulfill(1));

            } catch (Exception e) {}

        }

        // <--[tag]
        // @attribute <nation@nation.tag>
        // @returns Element
        // @description
        // Returns the nation's tag.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("tag")) {
            if (nation.hasTag())
                return new Element(nation.getTag())
                        .getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <nation@nation.taxes>
        // @returns Element(Decimal)
        // @description
        // Returns the nation's current taxes.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("taxes"))
            return new Element(nation.getTaxes())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <nation@nation.town_count>
        // @returns Element(Number)
        // @description
        // Returns the number of towns in the nation.
        // @plugin Towny
        // -->
        else if (attribute.startsWith("towncount") || attribute.startsWith("town_count"))
            return new Element(nation.getNumTowns())
                    .getAttribute(attribute.fulfill(1));

        return new Element(identify()).getAttribute(attribute.fulfill(0));

    }
}
