package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.areashop.*;
import com.denizenscript.depenizen.bukkit.properties.areashop.AreaShopPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class AreaShopBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(AreaShopTag.class);
        PropertyParser.registerProperty(AreaShopPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new AreaShopBoughtScriptEvent());
        ScriptEvent.registerScriptEvent(new AreaShopExpiresScriptEvent());
        ScriptEvent.registerScriptEvent(new AreaShopRentedScriptEvent());
        ScriptEvent.registerScriptEvent(new AreaShopResoldScriptEvent());
        ScriptEvent.registerScriptEvent(new AreaShopSoldScriptEvent());
    }
}
