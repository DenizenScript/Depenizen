package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.support.Support;

public class EffectLibSupport extends Support {

    public EffectLibSupport() {
        new EffectLibCommand().activate().as("effectlib").withOptions("See Documentation.", 1);
    }
}
