package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.dtltraders.TraderCommand;
import net.aufdemrand.denizen.utilities.DenizenAPI;

public class dtlTradersBridge extends Bridge {

    @Override
    public void init() {
        DenizenAPI.getCurrentInstance().getCommandRegistry().registerCoreMember(TraderCommand.class,
                "TRADER", "trader [open/close] ({buy}/sell) ({stock}/relation)", 1);
    }
}
