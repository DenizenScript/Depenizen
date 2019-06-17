package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.extensions.skillapi.SkillAPIPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerLevelsUpScriptEvent;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerUnlocksSkillScriptEvent;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerUpgradesSkillScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.skillapi.SkillAPIClass;
import net.aufdemrand.denizen.objects.dPlayer;
import com.denizenscript.depenizen.bukkit.events.skillapi.SkillAPIPlayerDowngradesSkillScriptEvent;

public class SkillAPISupport extends Support {

    public SkillAPISupport() {
        registerObjects(SkillAPIClass.class);
        registerProperty(SkillAPIPlayerExtension.class, dPlayer.class);
        registerScriptEvents(new SkillAPIPlayerUnlocksSkillScriptEvent());
        registerScriptEvents(new SkillAPIPlayerUpgradesSkillScriptEvent());
        registerScriptEvents(new SkillAPIPlayerDowngradesSkillScriptEvent());
        registerScriptEvents(new SkillAPIPlayerLevelsUpScriptEvent());
    }

}
