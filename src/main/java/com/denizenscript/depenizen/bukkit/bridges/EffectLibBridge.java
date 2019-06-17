package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;

    @Override
    public void init() {
        instance = this;
        new EffectLibCommand().activate().as("effectlib").withOptions("See Documentation.", 1);
    }
}
