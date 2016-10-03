package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperTradeScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.shopkeepers.ShopKeepersEntityExtension;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeper;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dEntity;

public class ShopKeepersSupport extends Support {

    public ShopKeepersSupport() {
        registerObjects(ShopKeeper.class);
        registerScriptEvents(new ShopKeeperTradeScriptEvent());
        registerProperty(ShopKeepersEntityExtension.class, dEntity.class);
    }

}
