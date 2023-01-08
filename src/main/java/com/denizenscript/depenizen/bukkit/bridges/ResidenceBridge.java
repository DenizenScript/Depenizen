package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.depenizen.bukkit.events.residence.*;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidenceLocationProperties;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidencePlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;

public class ResidenceBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(ResidenceTag.class, ResidenceTag.tagProcessor).generateBaseTag();
        ResidencePlayerProperties.register();
        ResidenceLocationProperties.register();
        ScriptEvent.registerScriptEvent(PlayerEntersResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerExitsResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerCreatesResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceDeletedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceRaidStartsScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceRaidEndsScriptEvent.class);

        // <--[tag]
        // @attribute <residence[<name>]>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns the ResidenceTag of given residence name.
        // -->
        TagManager.registerTagHandler(ResidenceTag.class, "residence", attribute -> {
            if (attribute.hasParam()) {
                return ResidenceTag.valueOf(attribute.getParam(), attribute.context);
            }
            return null;
        });
    }
}
