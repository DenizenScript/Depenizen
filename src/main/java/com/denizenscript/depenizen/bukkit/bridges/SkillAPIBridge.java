package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.properties.skillapi.SkillAPIPlayerProperties;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerLevelsUpScriptEvent;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerUnlocksSkillScriptEvent;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerUpgradesSkillScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClassTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerDowngradesSkillScriptEvent;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class SkillAPIBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(SkillAPIClassTag.class);
        PropertyParser.registerProperty(SkillAPIPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new SkillAPIPlayerUnlocksSkillScriptEvent());
        ScriptEvent.registerScriptEvent(new SkillAPIPlayerUpgradesSkillScriptEvent());
        ScriptEvent.registerScriptEvent(new SkillAPIPlayerDowngradesSkillScriptEvent());
        ScriptEvent.registerScriptEvent(new SkillAPIPlayerLevelsUpScriptEvent());
    }
}
