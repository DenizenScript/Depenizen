package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;

    @Override
    public void init() {
        instance = this;
        Denizen.getInstance().getCommandRegistry().registerCommand(EffectLibCommand.class);
    }
}
