package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.Depenizen;
import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;
import de.slikey.effectlib.EffectManager;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;
    public EffectManager effectManager;

    @Override
    public void init() {
        instance = this;
        effectManager = new EffectManager(Depenizen.instance);
        DenizenCore.commandRegistry.registerCommand(EffectLibCommand.class);
    }
}
