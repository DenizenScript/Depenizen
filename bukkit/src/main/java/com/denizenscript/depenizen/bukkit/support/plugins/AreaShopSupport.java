package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.events.areashop.AreaShopExpiresScriptEvent;
import com.denizenscript.depenizen.bukkit.extensions.areashop.AreaShopPlayerExtension;
import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import net.aufdemrand.denizen.objects.dPlayer;

public class AreaShopSupport extends Support {

    public AreaShopSupport() {
        registerObjects(dAreaShop.class);
        registerProperty(AreaShopPlayerExtension.class, dPlayer.class);
        registerScriptEvents(new AreaShopExpiresScriptEvent());
    }
}
