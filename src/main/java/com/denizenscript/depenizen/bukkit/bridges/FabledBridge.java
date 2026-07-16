package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.utilities.debugging.SlowWarning;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.fabled.*;
import com.denizenscript.depenizen.bukkit.objects.fabled.FabledClassTag;
import com.denizenscript.depenizen.bukkit.properties.fabled.FabledPlayerExtensions;

public class FabledBridge extends Bridge {

    public static SlowWarning oldSkillApiEvents = new SlowWarning("oldSkillApiEvents", "Events starting with 'skillapi' have been deprecated in favor of 'fabled'. See the meta site for more information.");

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(FabledClassTag.class, FabledClassTag.tagProcessor);
        FabledPlayerExtensions.register();
        ScriptEvent.registerScriptEvent(FabledPlayerUnlocksSkillScriptEvent.class);
        ScriptEvent.registerScriptEvent(FabledPlayerUpgradesSkillScriptEvent.class);
        ScriptEvent.registerScriptEvent(FabledPlayerDowngradesSkillScriptEvent.class);
        ScriptEvent.registerScriptEvent(FabledPlayerLevelsUpScriptEvent.class);

        // <--[tag]
        // @attribute <fabledclass[<class>]>
        // @returns FabledClassTag
        // @plugin Depenizen, Fabled
        // @description
        // Returns the fabledclass tag with the given name.
        // Refer to <@link objecttype FabledClassTag> for more information.
        // -->
        TagManager.registerTagHandler(FabledClassTag.class, FabledClassTag.class, "fabledclass", (attribute, param) -> {
            return param;
        });
    }
}
