package com.denizenscript.depenizen.bukkit.events.areashop;

import me.wiefferink.areashop.events.notify.UnrentedRegionEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopExpiresScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // areashop <'shop'> expires
    //
    // @Triggers when an AreaShop's rent expires.
    //
    // @Context
    // <context.shop> Returns the AreaShop that's expiring.
    //
    // @Plugin Depenizen, AreaShop
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public AreaShopExpiresScriptEvent() {
        registerCouldMatcher("areashop <'shop'> expires");
    }

    public UnrentedRegionEvent event;
    public AreaShopTag areaShop;

    @Override
    public boolean matches(ScriptPath path) {
        String shopName = path.eventArgLowerAt(2).replace("areashop@", "");
        if (!shopName.equals("shop") && !areaShop.equals(AreaShopTag.valueOf(shopName, getTagContext(path)))) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new PlayerTag(event.getOldRenter()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("shop")) {
            return areaShop;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onAreaShopExpires(UnrentedRegionEvent event) {
        if (event.getRefundedMoney() > 0) {
            return;
        }
        areaShop = new AreaShopTag(event.getRegion());
        this.event = event;
        fire(event);
    }
}
