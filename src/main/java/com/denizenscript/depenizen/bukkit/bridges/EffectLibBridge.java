package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenCore.commandRegistry.registerCommand(EffectLibCommand.class);
    }
}
