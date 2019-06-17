package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.commands.dtltraders.TraderCommand;

public class dtlTradersSupport extends Support {

    public dtlTradersSupport() {
        new TraderCommand().activate().as("trader").withOptions("trader [open/close] ({buy}/sell) ({stock}/relation)", 1);
    }
}
