package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.chestshop.*;

public class ChestShopBridge extends Bridge {

    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(ChestShopShopCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ChestShopShopDeletedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ChestShopTransactionScriptEvent.class);
    }

}
