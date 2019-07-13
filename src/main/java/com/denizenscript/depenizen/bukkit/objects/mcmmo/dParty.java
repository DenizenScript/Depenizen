package com.denizenscript.depenizen.bukkit.objects.mcmmo;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;
import com.denizenscript.denizen.objects.dPlayer;
import com.denizenscript.denizencore.objects.Element;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.dList;
import com.denizenscript.denizencore.objects.dObject;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.util.UUID;

public class dParty implements dObject {

    public static dParty valueOf(String string) {
        return dParty.valueOf(string, null);
    }

    @Fetchable("party")
    public static dParty valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        ////////
        // Match party name

        string = string.replace("party@", "");
        Party party = PartyManager.getParty(string);
        if (party == null) {
            return null;
        }
        return new dParty(party);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("party@", "");
        return PartyManager.getParty(arg) != null;
    }

    public static dParty forPlayer(dPlayer player) {
        McMMOPlayer pl = UserManager.getOfflinePlayer(player.getOfflinePlayer());
        if (pl == null) {
            return null;
        }
        Party party = pl.getParty();
        if (party == null) {
            return null;
        }
        return new dParty(party);
    }

    Party party = null;

    public dParty(Party party) {
        if (party != null) {
            this.party = party;
        }
        else {
            Debug.echoError("Party referenced is null!");
        }
    }

    String prefix = "Party";

    @Override
    public String getPrefix() {
        return prefix;
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
        return "Party";
    }

    @Override
    public String identify() {
        return "party@" + party.getName();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public dObject setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <party@party.name>
        // @returns Element
        // @description
        // Returns the name of the party.
        // @Plugin Depenizen, mcMMO
        // -->
        if (attribute.startsWith("name")) {
            return new Element(party.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <party@party.leader>
        // @returns dPlayer
        // @description
        // Returns the leader of the party.
        // @Plugin Depenizen, mcMMO
        // -->
        else if (attribute.startsWith("leader")) {
            return new dPlayer(party.getLeader().getUniqueId()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <party@party.members>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of all party members.
        // @Plugin Depenizen, mcMMO
        // -->
        else if (attribute.startsWith("members")) {
            dList players = new dList();
            for (UUID uuid : party.getMembers().keySet()) {
                players.add(dPlayer.valueOf(uuid.toString()).identify());
            }
            return players.getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
