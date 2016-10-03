package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.commands.McMMOCommands;
import com.morphanone.depenizenbukkit.events.mcmmo.mcMMOPlayerGainsXPScriptEvent;
import com.morphanone.depenizenbukkit.events.mcmmo.mcMMOPlayerLevelChangeScriptEvent;
import com.morphanone.depenizenbukkit.events.mcmmo.mcMMOPlayerLevelDownScriptEvent;
import com.morphanone.depenizenbukkit.events.mcmmo.mcMMOPlayerLevelUpScriptEvent;
import com.morphanone.depenizenbukkit.extensions.mcmmo.McMMOPlayerExtension;
import com.morphanone.depenizenbukkit.objects.dParty;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;
import com.morphanone.depenizenbukkit.support.Support;

public class McMMOSupport extends Support {

    public McMMOSupport() {
        registerObjects(dParty.class);
        registerAdditionalTags("party");
        registerProperty(McMMOPlayerExtension.class, dPlayer.class);
        new McMMOCommands().activate().as("MCMMO").withOptions("See Documentation.", 1);
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
