package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.residence.*;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidenceLocationExtensions;
import com.denizenscript.depenizen.bukkit.properties.residence.ResidencePlayerExtensions;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.objects.residence.ResidenceTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;

public class ResidenceBridge extends Bridge {

    @Override
    public void init() {

        // <--[tag]
        // @attribute <residence[<name>]>
        // @returns ResidenceTag
        // @plugin Depenizen, Residence
        // @description
        // Returns a residence object constructed from the input value.
        // Refer to <@link objecttype ResidenceTag>.
        // -->
        ObjectFetcher.registerWithObjectFetcher(ResidenceTag.class, ResidenceTag.tagProcessor).generateBaseTag();
        ResidencePlayerExtensions.register();
        ResidenceLocationExtensions.register();
        ScriptEvent.registerScriptEvent(PlayerEntersResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerExitsResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerCreatesResidenceScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceDeletedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceRaidStartsScriptEvent.class);
        ScriptEvent.registerScriptEvent(ResidenceRaidEndsScriptEvent.class);
    }
}
