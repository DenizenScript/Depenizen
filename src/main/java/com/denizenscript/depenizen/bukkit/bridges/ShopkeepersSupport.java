package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperTradeScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.shopkeepers.ShopKeepersEntityExtension;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeper;
import com.denizenscript.depenizen.bukkit.support.Support;
import net.aufdemrand.denizen.objects.dEntity;

public class ShopkeepersSupport extends Support {

    public ShopkeepersSupport() {
        registerObjects(ShopKeeper.class);
        registerScriptEvents(new ShopKeeperTradeScriptEvent());
        registerProperty(ShopKeepersEntityExtension.class, dEntity.class);
    }

}
