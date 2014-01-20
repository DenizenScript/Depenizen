package net.gnomeffinway.depenizen.objects;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.mcore.money.Money;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

public class dFaction implements dObject {

    /////////////////////
    //   OBJECT FETCHER
    /////////////////

    @Fetchable("faction")
    public static dFaction valueOf(String string) {
        if (string == null) return null;

        ////////
        // Match faction name

        string = string.replace("faction@", "");
        for (FactionColl fc : FactionColls.get().getColls()) {
            for (Faction f : fc.getAll()) {
                if (f.getComparisonName().equalsIgnoreCase(string)) {
                    return new dFaction(f);
                }
            }
        }

        return null;
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null)
            return true;

        return false;
    }

    /////////////////////
    //   STATIC CONSTRUCTORS
    /////////////////

    Faction faction = null;

    public dFaction(Faction faction) {
        if (faction != null)
            this.faction = faction;
        else
            dB.echoError("Faction referenced is null!");
    }

    public Faction getFaction() {
        return faction;
    }

    /////////////////////
    //   dObject Methods
    /////////////////

    private String prefix = "Faction";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public dObject setPrefix(String prefix) {
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
        return "Faction";
    }

    @Override
    public String identify() {
        return "faction@" + faction.getName();
    }

    @Override
    public String identifySimple() {
        // TODO: Properties?
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {

        // <--[tag]
        // @attribute <faction@faction.balance>
        // @returns Element(Double)
        // @description
        // Returns the amount of money the faction currently has.
        // @plugin Factions
        // -->
        if (attribute.startsWith("balance"))
            return new Element(Money.get(faction))
                    .getAttribute(attribute.fulfill(1));

            // <--[tag]
            // @attribute <faction@faction.home>
            // @returns dLocation
            // @description
            // Returns the location of the faction's home, if any.
            // @plugin Factions
            // -->
        else if (attribute.startsWith("home")) {
            if (faction.hasHome())
                return new dLocation(faction.getHome().asBukkitLocation())
                        .getAttribute(attribute.fulfill(1));
            else
                return new Element("null").getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <faction@faction.is_open>
        // @returns Element(Boolean)
        // @description
        // Returns true if the faction is open.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("isopen") || attribute.startsWith("is_open"))
            return new Element(faction.isOpen())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.is_peaceful>
        // @returns Element(Boolean)
        // @description
        // Returns true if the faction is peaceful.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("ispeaceful") || attribute.startsWith("is_peaceful"))
            return new Element(faction.getFlag(FFlag.PEACEFUL))
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.is_permanent>
        // @returns Element(Boolean)
        // @description
        // Returns true if the faction is permanent.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("ispermanent") || attribute.startsWith("is_permanent"))
            return new Element(faction.getFlag(FFlag.PERMANENT))
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.leader>
        // @returns dPlayer
        // @description
        // Returns the faction's leader as a dPlayer.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("leader"))
            return new dPlayer(faction.getLeader().getPlayer())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.name>
        // @returns Element
        // @description
        // Returns the name of the faction.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("name"))
            return new Element(faction.getName())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.player_count>
        // @returns Element(Integer)
        // @description
        // Returns the number of players in the faction.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
            return new Element(faction.getUPlayers().size())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.power>
        // @returns Element(Double)
        // @description
        // Returns the amount of power the faction currently has.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("power"))
            return new Element(faction.getPower())
                    .getAttribute(attribute.fulfill(1));

        // <--[tag]
        // @attribute <faction@faction.relation[<faction>]>
        // @returns Element
        // @description
        // Returns the current relation between the faction and another faction.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("relation")) {
            dFaction to = valueOf(attribute.getContext(1));

            if (to != null)
                return new Element(faction.getRelationTo(to.getFaction()).toString())
                        .getAttribute(attribute.fulfill(1));
            else
                return new Element("null").getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <faction@faction.size>
        // @returns Element(Integer)
        // @description
        // Returns the amount of land the faction has.
        // @plugin Factions
        // -->
        else if (attribute.startsWith("size"))
            return new Element(faction.getLandCount())
                    .getAttribute(attribute.fulfill(1));
            // <--[tag]
            // @attribute <faction@faction.leader>
            // @returns Element(String)
            // @description
            // Returns the name of the faction leader.
            // @plugin Factions
            // -->
        else if (attribute.startsWith("leader"))
            return new Element(faction.getLeader().getName())
                    .getAttribute(attribute.fulfill(1));

        return new Element(identify()).getAttribute(attribute);

    }

}
