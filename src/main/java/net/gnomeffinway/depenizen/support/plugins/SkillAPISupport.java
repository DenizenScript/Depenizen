package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dPlayer;
import net.gnomeffinway.depenizen.events.SkillAPIEvents;
import net.gnomeffinway.depenizen.extensions.skillapi.SkillAPIPlayerExtension;
import net.gnomeffinway.depenizen.objects.skillapi.SkillAPIClass;
import net.gnomeffinway.depenizen.support.Support;

public class SkillAPISupport extends Support {

    public SkillAPISupport() {
        registerObjects(SkillAPIClass.class);
        registerProperty(SkillAPIPlayerExtension.class, dPlayer.class);
        registerEvents(SkillAPIEvents.class);
    }

}
