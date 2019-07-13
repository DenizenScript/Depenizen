package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.utilities.DenizenAPI;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(EffectLibCommand.class,
                "EFFECTLIB", "effectlib (type:<effect name>) (duration:<duration>) (target:<entity>)", 1);
    }
}
