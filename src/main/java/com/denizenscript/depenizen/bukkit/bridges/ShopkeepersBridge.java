package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperTradeScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.shopkeepers.ShopKeepersEntityProperties;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeper;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.properties.PropertyParser;

public class ShopkeepersBridge extends Bridge {

    public static ShopkeepersBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(ShopKeeper.class);
        ScriptEvent.registerScriptEvent(new ShopKeeperTradeScriptEvent());
        PropertyParser.registerProperty(ShopKeepersEntityProperties.class, EntityTag.class);
    }
}
