package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.breweryx.BreweryChatDistortScriptEvent;
import com.denizenscript.depenizen.bukkit.events.breweryx.BreweryDrinkScriptEvent;
import com.denizenscript.depenizen.bukkit.events.breweryx.BreweryIngredientAddScriptEvent;
import com.denizenscript.depenizen.bukkit.events.breweryx.BreweryModifyBrewScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BPlayerTag;
import com.denizenscript.depenizen.bukkit.objects.breweryx.BRecipeTag;

public class BreweryXBridge extends Bridge {
    @Override
    public void init() {
        ScriptEvent.registerScriptEvent(BreweryDrinkScriptEvent.class);
        ScriptEvent.registerScriptEvent(BreweryModifyBrewScriptEvent.class);
        ScriptEvent.registerScriptEvent(BreweryIngredientAddScriptEvent.class);
        ScriptEvent.registerScriptEvent(BreweryChatDistortScriptEvent.class);
        ObjectFetcher.registerWithObjectFetcher(BPlayerTag.class);
        ObjectFetcher.registerWithObjectFetcher(BRecipeTag.class);
    }
}
