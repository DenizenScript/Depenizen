package com.denizenscript.depenizen.bukkit.objects;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.utilities.debugging.dB;

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
            dB.echoError("Party referenced is null!");
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <party@party.name>
        // @returns Element
        // @description
        // Returns the name of the party.
        // @plugin Depenizen, mcMMO
        // -->
        if (attribute.startsWith("name")) {
            return new Element(party.getName()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <party@party.leader>
        // @returns dPlayer
        // @description
        // Returns the leader of the party.
        // @plugin Depenizen, mcMMO
        // -->
        else if (attribute.startsWith("leader")) {
            return new dPlayer(party.getLeader().getUniqueId()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <party@party.members>
        // @returns dList(dPlayer)
        // @description
        // Returns a list of all party members.
        // @plugin Depenizen, mcMMO
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
