package net.gnomeffinway.depenizen.extensions.simpleclans;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.extensions.dObjectExtension;
import net.gnomeffinway.depenizen.objects.simpleclans.dClan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

public class SimpleClansPlayerExtension extends dObjectExtension {

    public static boolean describes(dObject pl) {
        return pl instanceof dPlayer;
    }

    public static SimpleClansPlayerExtension getFrom(dObject pl) {
        if (!describes(pl)) {
            return null;
        }
        else {
            return new SimpleClansPlayerExtension((dPlayer) pl);
        }
    }

    private SimpleClansPlayerExtension(dPlayer pl) {
        this.player = pl;
        this.cplayer = new ClanPlayer(pl.getOfflinePlayer().getUniqueId());
    }

    dPlayer player;
    ClanPlayer cplayer;


    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("simpleclans")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <p@player.simpleclans.clan>
            // @returns dClan
            // @description
            // Returns the clan the player is in, if any.
            // @plugin Depenizen, SimpleClans
            // -->
            if (attribute.startsWith("clan")) {
                dClan clan = dClan.forPlayer(player);
                if (clan == null) {
                    return null;
                }
                return clan.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.simpleclans.has_clan>
            // @returns dClan
            // @description
            // Returns whether the player is in a clan.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("has_clan")) {
                return new Element(dClan.forPlayer(player) != null).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.simpleclans.kill_death_ratio>
            // @returns Element(Decimal)
            // @description
            // Returns the player's kill-death ratio.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("kill_death_ratio")) {
                return new Element(cplayer.getKDR()).getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <p@player.simpleclans.deaths>
            // @returns Element(Number)
            // @description
            // Returns the number of deaths the player has.
            // @plugin Depenizen, SimpleClans
            // -->
            else if (attribute.startsWith("deaths")) {
                return new Element(cplayer.getDeaths()).getAttribute(attribute.fulfill(1));
            }

            else if (attribute.startsWith("kills")) {
                attribute = attribute.fulfill(1);

                // <--[tag]
                // @attribute <p@player.simpleclans.kills.civilian>
                // @returns Element(Number)
                // @description
                // Returns the total number of civilian kills the player has.
                // @plugin Depenizen, SimpleClans
                // -->
                if (attribute.startsWith("civilian")) {
                    return new Element(cplayer.getCivilianKills()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.simpleclans.kills.rivals>
                // @returns Element(Number)
                // @description
                // Returns the total number of rival kills the player has.
                // @plugin Depenizen, SimpleClans
                // -->
                else if (attribute.startsWith("rivals")) {
                    return new Element(cplayer.getRivalKills()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.simpleclans.kills.neutral>
                // @returns Element(Number)
                // @description
                // Returns the total number of neutral kills the player has.
                // @plugin Depenizen, SimpleClans
                // -->
                else if (attribute.startsWith("neutral")) {
                    return new Element(cplayer.getNeutralKills()).getAttribute(attribute.fulfill(1));
                }

                // <--[tag]
                // @attribute <p@player.simpleclans.kills.total>
                // @returns Element(Number)
                // @description
                // Returns the total number of kills the player has.
                // @plugin Depenizen, SimpleClans
                // -->
                else if (attribute.startsWith("total")) {
                    return new Element(cplayer.getCivilianKills()
                            + cplayer.getRivalKills()
                            + cplayer.getNeutralKills()).getAttribute(attribute.fulfill(1));
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        return null;
    }
}
