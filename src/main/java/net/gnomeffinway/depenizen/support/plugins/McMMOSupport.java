package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import net.gnomeffinway.depenizen.commands.McMMOCommands;
import net.gnomeffinway.depenizen.events.mcMMO.mcMMOPlayerGainsXPScriptEvent;
import net.gnomeffinway.depenizen.events.mcMMO.mcMMOPlayerLevelChangeScriptEvent;
import net.gnomeffinway.depenizen.events.mcMMO.mcMMOPlayerLevelDownScriptEvent;
import net.gnomeffinway.depenizen.events.mcMMO.mcMMOPlayerLevelUpScriptEvent;
import net.gnomeffinway.depenizen.extensions.mcmmo.McMMOPlayerExtension;
import net.gnomeffinway.depenizen.objects.dParty;
import net.gnomeffinway.depenizen.support.Support;

public class McMMOSupport extends Support {

    public McMMOSupport() {
        registerObjects(dParty.class);
        registerAdditionalTags("party");
        registerProperty(McMMOPlayerExtension.class, dPlayer.class);
        new McMMOCommands().activate().as("MCMMO").withOptions("see documentation", 1);
        registerScriptEvents(new mcMMOPlayerLevelChangeScriptEvent());
        registerScriptEvents(new mcMMOPlayerLevelUpScriptEvent());
        registerScriptEvents(new mcMMOPlayerLevelDownScriptEvent());
        registerScriptEvents(new mcMMOPlayerGainsXPScriptEvent());
    }

    public String additionalTags(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("party") && attribute.hasContext(1)) {
            dParty party = dParty.valueOf(attribute.getContext(1));
            if (party == null) {
                return null;
            }
            return party.getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
