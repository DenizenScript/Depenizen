package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.depenizen.bukkit.commands.mcmmo.McMMOCommand;
import com.denizenscript.depenizen.bukkit.events.mcmmo.*;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOEntityProperties;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.mcmmo.PartyTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.tags.TagManager;

public class McMMOBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(PartyTag.class);
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                tagEvent(event);
            }
        }, "party");
        PropertyParser.registerProperty(McMMOPlayerProperties.class, PlayerTag.class);
        PropertyParser.registerProperty(McMMOEntityProperties.class, EntityTag.class);
        PropertyParser.registerProperty(McMMOLocationProperties.class, LocationTag.class);
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(McMMOCommand.class);
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
            PartyTag party = attribute.contextAsType(1, PartyTag.class);
            if (party != null) {
                event.setReplacedObject(party.getObjectAttribute(attribute.fulfill(1)));
            }
            else if (!event.hasAlternative()) {
                Debug.echoError("Unknown party '" + attribute.getContext(1) + "' for party[] tag.");
            }
        }
    }
}
