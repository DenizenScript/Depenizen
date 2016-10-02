package net.gnomeffinway.depenizen.support.plugins;

import net.gnomeffinway.depenizen.events.areashop.AreaShopExpiresScriptEvent;
import net.gnomeffinway.depenizen.extensions.areashop.AreaShopPlayerExtension;
import net.gnomeffinway.depenizen.objects.areashop.dAreaShop;
import net.gnomeffinway.depenizen.support.Support;

public class AreaShopSupport extends Support {

    public AreaShopSupport() {
        registerObjects(dAreaShop.class);
        registerProperty(AreaShopPlayerExtension.class);
        registerScriptEvents(new AreaShopExpiresScriptEvent());
    }
}
