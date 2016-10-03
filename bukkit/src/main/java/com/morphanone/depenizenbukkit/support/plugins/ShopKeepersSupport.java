package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.extensions.shopkeepers.ShopKeepersEntityExtension;
import net.aufdemrand.denizen.objects.dEntity;
import com.morphanone.depenizenbukkit.events.shopkeepers.ShopKeeperTradeScriptEvent;
import com.morphanone.depenizenbukkit.objects.shopkeepers.ShopKeeper;
import com.morphanone.depenizenbukkit.support.Support;

public class ShopKeepersSupport extends Support {

    public ShopKeepersSupport() {
        registerObjects(ShopKeeper.class);
        registerScriptEvents(new ShopKeeperTradeScriptEvent());
        registerProperty(ShopKeepersEntityExtension.class, dEntity.class);
    }

}
