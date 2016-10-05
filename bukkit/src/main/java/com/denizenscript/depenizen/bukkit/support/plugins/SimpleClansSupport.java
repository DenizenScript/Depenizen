package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.extensions.simpleclans.SimpleClansPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.dClan;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SimpleClansSupport extends Support {

    SimpleClans sc;

    public SimpleClansSupport() {
        registerObjects(dClan.class);
        registerProperty(SimpleClansPlayerExtension.class, dPlayer.class);
        registerAdditionalTags("simpleclans");
        registerAdditionalTags("clan");
        sc = SimpleClans.getInstance();
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute.startsWith("clan") && attribute.hasContext(1)) {
            dClan clan = dClan.valueOf(attribute.getContext(1));
            if (clan == null) {
                return null;
            }
            return clan.getAttribute(attribute.fulfill(1));
        }

        else if (attribute.startsWith("simpleclans")) {
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <simpleclans.list_clans>
            // @returns dList(dClan)
            // @description
            // Returns a list of all clans.
            // @Plugin DepenizenBukkit, SimpleClans
            // -->
            if (attribute.startsWith("list_clans")) {
                dList clans = new dList();
                for (Clan cl : sc.getClanManager().getClans()) {
                    clans.add(new dClan(cl).identify());
                }
                return clans.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <simpleclans.list_unverified_clans>
            // @returns dList(dClan)
            // @description
            // Returns a list of all unverified clans.
            // @Plugin DepenizenBukkit, SimpleClans
            // -->
            else if (attribute.startsWith("list_unverified_clans")) {
                dList clans = new dList();
                for (Clan cl : sc.getClanManager().getClans()) {
                    if (!cl.isVerified()) {
                        clans.add(new dClan(cl).identify());
                    }
                }
                return clans.getAttribute(attribute.fulfill(1));
            }

            // <--[tag]
            // @attribute <simpleclans.list_verified_clans>
            // @returns dList(dClan)
            // @description
            // Returns a list of all verified clans.
            // @Plugin DepenizenBukkit, SimpleClans
            // -->
            else if (attribute.startsWith("list_verified_clans")) {
                dList clans = new dList();
                for (Clan cl : sc.getClanManager().getClans()) {
                    if (cl.isVerified()) {
                        clans.add(new dClan(cl).identify());
                    }
                }
                return clans.getAttribute(attribute.fulfill(1));
            }
        }
        return null;
    }

}
