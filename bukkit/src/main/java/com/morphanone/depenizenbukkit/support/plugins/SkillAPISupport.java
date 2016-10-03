package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.skillapi.SkillAPIPlayerLevelsUpScriptEvent;
import com.morphanone.depenizenbukkit.events.skillapi.SkillAPIPlayerUnlocksSkillScriptEvent;
import com.morphanone.depenizenbukkit.events.skillapi.SkillAPIPlayerUpgradesSkillScriptEvent;
import com.morphanone.depenizenbukkit.objects.skillapi.SkillAPIClass;
import net.aufdemrand.denizen.objects.dPlayer;
import com.morphanone.depenizenbukkit.events.skillapi.SkillAPIPlayerDowngradesSkillScriptEvent;
import com.morphanone.depenizenbukkit.extensions.skillapi.SkillAPIPlayerExtension;
import com.morphanone.depenizenbukkit.support.Support;

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
