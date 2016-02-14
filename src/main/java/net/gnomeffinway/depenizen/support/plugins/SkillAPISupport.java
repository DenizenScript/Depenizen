package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.skillapi.SkillAPIPlayerDowngradesSkillScriptEvent;
import net.gnomeffinway.depenizen.events.skillapi.SkillAPIPlayerLevelsUpScriptEvent;
import net.gnomeffinway.depenizen.events.skillapi.SkillAPIPlayerUnlocksSkillScriptEvent;
import net.gnomeffinway.depenizen.events.skillapi.SkillAPIPlayerUpgradesSkillScriptEvent;
import net.gnomeffinway.depenizen.extensions.skillapi.SkillAPIPlayerExtension;
import net.gnomeffinway.depenizen.objects.skillapi.SkillAPIClass;
import net.gnomeffinway.depenizen.support.Support;

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
