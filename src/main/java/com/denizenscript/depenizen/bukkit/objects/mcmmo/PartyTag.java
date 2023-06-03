package com.denizenscript.depenizen.bukkit.objects.mcmmo;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import java.util.UUID;

public class PartyTag implements ObjectTag {

    // <--[ObjectType]
    // @name PartyTag
    // @prefix party
    // @base ElementTag
    // @format
    // The identity format for parties is <party_name>
    // For example, 'party@my_party'.
    //
    // @plugin Depenizen, McMMO
    // @description
    // A PartyTag represents an McMMO party.
    //
    // -->

    @Fetchable("party")
    public static PartyTag valueOf(String string, TagContext context) {
        if (string.startsWith("party@")) {
            string = string.substring("party@".length());
        }
        Party party = PartyManager.getParty(string);
        if (party == null) {
            return null;
        }
        return new PartyTag(party);
    }

    public static boolean matches(String arg) {
        arg = arg.replace("party@", "");
        return PartyManager.getParty(arg) != null;
    }

    public static PartyTag forPlayer(PlayerTag player) {
        McMMOPlayer pl = UserManager.getOfflinePlayer(player.getOfflinePlayer());
        if (pl == null) {
            return null;
        }
        Party party = pl.getParty();
        if (party == null) {
            return null;
        }
        return new PartyTag(party);
    }

    Party party = null;

    public PartyTag(Party party) {
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
    public boolean isUnique() {
        return true;
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
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <PartyTag.name>
        // @returns ElementTag
        // @plugin Depenizen, mcMMO
        // @description
        // Returns the name of the party.
        // -->
        if (attribute.startsWith("name")) {
            return new ElementTag(party.getName()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PartyTag.leader>
        // @returns PlayerTag
        // @plugin Depenizen, mcMMO
        // @description
        // Returns the leader of the party.
        // -->
        else if (attribute.startsWith("leader")) {
            return new PlayerTag(party.getLeader().getUniqueId()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <PartyTag.members>
        // @returns ListTag(PlayerTag)
        // @plugin Depenizen, mcMMO
        // @description
        // Returns a list of all party members.
        // -->
        else if (attribute.startsWith("members")) {
            ListTag players = new ListTag();
            for (UUID uuid : party.getMembers().keySet()) {
                players.addObject(new PlayerTag(uuid));
            }
            return players.getObjectAttribute(attribute.fulfill(1));
        }

        return new ElementTag(identify()).getObjectAttribute(attribute);
    }
}
