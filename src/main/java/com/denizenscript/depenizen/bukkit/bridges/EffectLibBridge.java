package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.commands.effectlib.EffectLibCommand;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.utilities.DenizenAPI;

public class EffectLibBridge extends Bridge {

    public static EffectLibBridge instance;

    @Override
    public void init() {
        instance = this;
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCommand(EffectLibCommand.class);
    }
}
