package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.commands.mcmmo.McMMOCommand;
import com.denizenscript.depenizen.bukkit.events.mcmmo.*;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOEntityProperties;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.mcmmo.McMMOPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.mcmmo.PartyTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
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
        DenizenCore.commandRegistry.registerCommand(McMMOCommand.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerLevelChangeScriptEvent.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerLevelUpScriptEvent.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerLevelDownScriptEvent.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerGainsXPScriptEvent.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerAbilityActivateScriptEvent.class);
        ScriptEvent.registerScriptEvent(mcMMOPlayerAbilityDeactivateScriptEvent.class);
    }

    public void tagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes();

        // <--[tag]
        // @attribute <party[<party>]>
        // @returns PartyTag
        // @plugin Depenizen, mcMMO
        // @description
        // Returns a party object constructed from the input value.
        // Refer to <@link objecttype PartyTag>.
        // -->
        if (attribute.startsWith("party") && attribute.hasParam()) {
            PartyTag party = attribute.paramAsType(PartyTag.class);
            if (party != null) {
                event.setReplacedObject(party.getObjectAttribute(attribute.fulfill(1)));
            }
            else {
                attribute.echoError("Unknown party '" + attribute.getParam() + "' for party[] tag.");
            }
        }
    }
}
