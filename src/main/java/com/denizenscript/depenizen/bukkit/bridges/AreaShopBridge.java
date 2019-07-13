package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.areashop.AreaShopExpiresScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.areashop.AreaShopPlayerProperties;
import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class AreaShopBridge extends Bridge {

    @Override
    public void init() {
        ObjectFetcher.registerWithObjectFetcher(dAreaShop.class);
        PropertyParser.registerProperty(AreaShopPlayerProperties.class, PlayerTag.class);
        ScriptEvent.registerScriptEvent(new AreaShopExpiresScriptEvent());
    }
}
