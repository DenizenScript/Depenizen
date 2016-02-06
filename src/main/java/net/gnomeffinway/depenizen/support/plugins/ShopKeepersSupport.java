package net.gnomeffinway.depenizen.support.plugins;

import net.aufdemrand.denizen.objects.dEntity;
import net.gnomeffinway.depenizen.events.ShopKeepers.ShopKeeperTradeScriptEvent;
import net.gnomeffinway.depenizen.extensions.ShopKeepers.ShopKeepersEntityExtension;
import net.gnomeffinway.depenizen.objects.ShopKeepers.ShopKeeper;
import net.gnomeffinway.depenizen.support.Support;

public class ShopKeepersSupport extends Support {

    public ShopKeepersSupport() {
        registerObjects(ShopKeeper.class);
        registerScriptEvents(new ShopKeeperTradeScriptEvent());
        registerProperty(ShopKeepersEntityExtension.class, dEntity.class);
    }

}
