package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.simpleclans.SimpleClansPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.dClan;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagManager;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SimpleClansBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(dClan.class);
        PropertyParser.registerProperty(SimpleClansPlayerProperties.class, dPlayer.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "simpleclans");
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                clanTagEvent(event);
            }
        }, "clan");
    }

    public void clanTagEvent(ReplaceableTagEvent event) {
        // <--[tag]
        // @attribute <clan[<name>]>
        // @returns dClan
        // @description
        // Returns the clan for the input name.
        // @Plugin Depenizen, SimpleClans
        // -->
        Attribute attribute = event.getAttributes();
        if (attribute.startsWith("clan") && attribute.hasContext(1)) {
            dClan clan = dClan.valueOf(attribute.getContext(1));
            if (clan != null) {
                event.setReplacedObject(clan.getObjectAttribute(attribute.fulfill(1)));
            }
            else if (!event.hasAlternative()) {
                dB.echoError("Unknown clan '" + attribute.getContext(1) + "' for clan[] tag.");
            }
        }
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <simpleclans.list_clans>
        // @returns dList(dClan)
        // @description
        // Returns a list of all clans.
        // @Plugin Depenizen, SimpleClans
        // -->
        if (attribute.startsWith("list_clans")) {
            dList clans = new dList();
            for (Clan cl : ((SimpleClans) plugin).getClanManager().getClans()) {
                clans.add(new dClan(cl).identify());
            }
            event.setReplacedObject(clans.getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <simpleclans.list_unverified_clans>
        // @returns dList(dClan)
        // @description
        // Returns a list of all unverified clans.
        // @Plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("list_unverified_clans")) {
            dList clans = new dList();
            for (Clan cl : ((SimpleClans) plugin).getClanManager().getClans()) {
                if (!cl.isVerified()) {
                    clans.add(new dClan(cl).identify());
                }
            }
            event.setReplacedObject(clans.getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <simpleclans.list_verified_clans>
        // @returns dList(dClan)
        // @description
        // Returns a list of all verified clans.
        // @Plugin Depenizen, SimpleClans
        // -->
        else if (attribute.startsWith("list_verified_clans")) {
            dList clans = new dList();
            for (Clan cl : ((SimpleClans) plugin).getClanManager().getClans()) {
                if (cl.isVerified()) {
                    clans.add(new dClan(cl).identify());
                }
            }
            event.setReplacedObject(clans.getObjectAttribute(attribute.fulfill(1)));
        }
    }
}
