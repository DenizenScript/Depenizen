package net.gnomeffinway.depenizen.support.plugins;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.party.PartyManager;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.Attribute;
import net.gnomeffinway.depenizen.commands.McMMOCommands;
import net.gnomeffinway.depenizen.extensions.mcmmo.McMMOPlayerExtension;
import net.gnomeffinway.depenizen.support.Support;

public class McMMOSupport extends Support {

    public McMMOSupport() {
        registerAdditionalTags("party");
        registerProperty(McMMOPlayerExtension.class, dPlayer.class);
        new McMMOCommands().activate().as("MCMMO").withOptions("see documentation", 1);
    }

    @Override
    public String additionalTags(Attribute attribute) {

        if (attribute.startsWith("party")) {

            if (!attribute.hasContext(1) || PartyManager.getParty(attribute.getContext(1)) == null)
                return null;

            Party party = PartyManager.getParty(attribute.getContext(1));
            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <party[<party>].leader>
            // @returns dPlayer
            // @description
            // Returns the leader of the party.
            // @plugin Depenizen, mcMMO
            // -->
            if (attribute.startsWith("leader"))
                return dPlayer.valueOf(party.getLeader()).getAttribute(attribute.fulfill(1));

            // <--[tag]
            // @attribute <party[<party>].player_count>
            // @returns Element(Integer)
            // @description
            // Returns the number of players in the party.
            // @plugin Depenizen, mcMMO
            // -->
            else if (attribute.startsWith("playercount") || attribute.startsWith("player_count"))
                return new Element(party.getMembers().size()).getAttribute(attribute.fulfill(1));

        }

        return null;

    }
}
