package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.McMMOCommands;
import com.denizenscript.depenizen.bukkit.events.mcmmo.*;
import com.denizenscript.depenizen.bukkit.extensions.mcmmo.McMMOPlayerExtension;
import com.denizenscript.depenizen.bukkit.objects.dParty;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.Attribute;

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
        registerScriptEvents(new mcMMOPlayerAbilityActivateScriptEvent());
        registerScriptEvents(new mcMMOPlayerAbilityDeactivateScriptEvent());
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
