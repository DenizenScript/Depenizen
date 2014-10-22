package net.gnomeffinway.depenizen.support.plugins;

import net.gnomeffinway.depenizen.events.SkillAPIEvents;
import net.gnomeffinway.depenizen.support.Support;

public class SkillAPISupport extends Support {

    public SkillAPISupport() {
        registerEvents(SkillAPIEvents.class);
    }

}
