package net.gnomeffinway.depenizen.objects.simpleclans;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class dClan implements dObject {

    public static dClan valueOf(String tag) {
        return dClan.valueOf(tag, null);
    }

    @Fetchable("clan")
    public static dClan valueOf(String tag, TagContext context) {
        if (tag == null) {
            return null;
        }
        ////////
        // Match clan name
        tag = tag.replace("clan@", "");
        Clan cl = SimpleClans.getInstance().getClanManager().getClan(tag);
        if (cl == null) {
            return null;
        }
        return new dClan(cl);
    }

    public static dClan forPlayer(dPlayer pl) {
        Clan cl = SimpleClans.getInstance().getClanManager().getClanByPlayerUniqueId(pl.getOfflinePlayer().getUniqueId());
        if (cl == null) {
            return null;
        }
        return new dClan(cl);
    }

    public static boolean matches(String tag) {
        return valueOf(tag) != null;
    }

    public dClan(Clan c) {
        if (c != null) {
            clan = c;
        }
        else {
            dB.echoError("Clan referenced is null");
        }
    }

    Clan clan = null;

    private String prefix = "Clan";

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Clan";
    }

    @Override
    public String identify() {
        return "clan@" + clan.getTag();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public dObject setPrefix(String s) {
        this.prefix = s;
        return this;
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <clan@clan.leaders>
        // @returns dList(dPlayer)
        // @description
        // Returns the leaders of the clan.
        // @plugin Depenizen, SimpleClans
        // -->
        if (attribute.startsWith("leaders")) {
            dList leaders = new dList();
            for (ClanPlayer p : clan.getLeaders()) {
                leaders.add(new dPlayer(p.getUniqueId()).identify());
            }
            return leaders.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <clan@clan.members>
        // @returns dList(dPlayer)
        // @description
        // Returns all the members of the clan (including leaders).
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("members")) {
            dList members = new dList();
            for (ClanPlayer p : clan.getMembers()) {
                members.add(new dPlayer(p.toPlayer()).identify());
            }
            return members.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <clan@clan.is_verified>
        // @returns Element(Boolean)
        // @description
        // Returns the whether the clan is verified.
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("is_verified")) {
            return new Element(clan.isVerified()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <clan@clan.allies>
        // @returns dList(dClan)
        // @description
        // Returns the ally clans of the clan.
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("allies")) {
            dList allies = new dList();
            for (String c : clan.getAllies()) {
                allies.add(dClan.valueOf(c).identify());
            }
            return allies.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <clan@clan.rivals>
        // @returns dList(dClan)
        // @description
        // Returns the rival clans of the clan.
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("rivals")) {
            dList rivals = new dList();
            for (String c : clan.getRivals()) {
                rivals.add(dClan.valueOf(c).identify());
            }
            return rivals.getAttribute(attribute.fulfill(1));
        }


        // <--[tag]
        // @attribute <clan@clan.deaths>
        // @returns Element(Number)
        // @description
        // Returns the total number of deaths the clan has.
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("deaths")) {
            return new Element(clan.getTotalDeaths()).getAttribute(attribute.fulfill(1));
        }

        else if (attribute.startsWith("kills")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <clan@clan.kills.civilian>
            // @returns Element(Number)
            // @description
            // Returns the total number of civilian kills the clan has.
            // @plugin Depenizen, SimpleClans
            // -->
            if (attribute.startsWith("civilian")) {
                return new Element(clan.getTotalCivilian()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <clan@clan.kills.rivals>
            // @returns Element(Number)
            // @description
            // Returns the total number of rival kills the clan has.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("rivals")) {
                return new Element(clan.getTotalRival()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <clan@clan.kills.neutral>
            // @returns Element(Number)
            // @description
            // Returns the total number of neutral kills the clan has.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("neutral")) {
                return new Element(clan.getTotalNeutral()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <clan@clan.kills.total>
            // @returns Element(Number)
            // @description
            // Returns the total number of kills the clan has.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("total")) {
                return new Element(clan.getTotalCivilian()
                        + clan.getTotalRival()
                        + clan.getTotalNeutral()).getAttribute(attribute.fulfill(1));
            }
            else {
                return null;
            }
        }

        // <--[tag]
        // @attribute <clan@clan.kill_death_ratio>
        // @returns Element(Decimal)
        // @description
        // Returns the kill death ratio of the clan.
        // @plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("kill_death_ratio")) {
            return new Element(clan.getTotalKDR()).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
