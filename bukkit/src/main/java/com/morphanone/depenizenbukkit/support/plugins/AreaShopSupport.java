package com.morphanone.depenizenbukkit.support.plugins;

import com.morphanone.depenizenbukkit.events.areashop.AreaShopExpiresScriptEvent;
import com.morphanone.depenizenbukkit.extensions.areashop.AreaShopPlayerExtension;
import com.morphanone.depenizenbukkit.objects.areashop.dAreaShop;
import com.morphanone.depenizenbukkit.support.Support;

public class AreaShopSupport extends Support {

    public AreaShopSupport() {
        registerObjects(dAreaShop.class);
        registerProperty(AreaShopPlayerExtension.class);
        registerScriptEvents(new AreaShopExpiresScriptEvent());
    }
}
