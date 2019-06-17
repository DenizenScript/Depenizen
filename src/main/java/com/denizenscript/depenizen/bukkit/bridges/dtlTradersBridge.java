package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.dtltraders.TraderCommand;

public class dtlTradersBridge extends Bridge {

    @Override
    public void init() {
        new TraderCommand().activate().as("trader").withOptions("trader [open/close] ({buy}/sell) ({stock}/relation)", 1);
    }
}
