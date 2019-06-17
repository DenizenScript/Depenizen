package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.mcmmo.McMMOCommand;
import com.denizenscript.depenizen.bukkit.events.mcmmo.*;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.dParty;
import com.denizenscript.depenizen.bukkit.Bridge;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.ObjectFetcher;
import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.properties.PropertyParser;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;

public class McMMOBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(dParty.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "party");
        PropertyParser.registerProperty(McMMOPlayerProperties.class, dPlayer.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(McMMOCommand.class,
                "MCMMO", "mcmmo [add/remove/set] [levels/xp/xprate/vampirism/hardcore/leader] (skill:<skill>) (state:{toggle}/true/false) (qty:<#>) (party:<party>)", 1);
        ScriptEvent.registerScriptEvent(new mcMMOPlayerLevelChangeScriptEvent());
        ScriptEvent.registerScriptEvent(new mcMMOPlayerLevelUpScriptEvent());
        ScriptEvent.registerScriptEvent(new mcMMOPlayerLevelDownScriptEvent());
        ScriptEvent.registerScriptEvent(new mcMMOPlayerGainsXPScriptEvent());
        ScriptEvent.registerScriptEvent(new mcMMOPlayerAbilityActivateScriptEvent());
        ScriptEvent.registerScriptEvent(new mcMMOPlayerAbilityDeactivateScriptEvent());
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        if (attribute.startsWith("party") && attribute.hasContext(1)) {
            dParty party = dParty.valueOf(attribute.getContext(1));
            if (party != null) {
                event.setReplacedObject(party.getObjectAttribute(attribute.fulfill(1)));
            }
            else if (!event.hasAlternative()) {
                dB.echoError("Unknown party '" + attribute.getContext(1) + "' for party[] tag.");
            }
        }
    }
}
