package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.dre.brewery.api.events.brew.BrewDrinkEvent;

public class BreweryXBridge extends Bridge {
    @Override
    public void init() {
        ScriptEvent.registerScriptEvent();
    }
}
