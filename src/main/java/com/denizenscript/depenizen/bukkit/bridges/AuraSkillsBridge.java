package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.auraskills.AuraSkillsPlayerExtensions;

public class AuraSkillsBridge extends Bridge {

    @Override
    public void init() {
        AuraSkillsPlayerExtensions.register();
    }
}
