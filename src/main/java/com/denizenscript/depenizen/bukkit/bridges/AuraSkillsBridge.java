package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.properties.auraskills.AuraSkillsPlayerProperties;

public class AuraSkillsBridge extends Bridge {

    @Override
    public void init() {
        AuraSkillsPlayerProperties.register();
    }
}
