package com.denizenscript.depenizen.bukkit.events.areashop;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import me.wiefferink.areashop.events.notify.SoldRegionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopSoldScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // areashop <'shop'> sold
    //
    // @Triggers when an AreaShop is resold.
    //
    // @Context
    // <context.shop> returns the AreaShop that's being sold.
    //
    // @Plugin Depenizen, AreaShop
    //
    // @Player Always (the old buyer).
    //
    // @Group Depenizen
    //
    // -->

    public AreaShopSoldScriptEvent() {
        registerCouldMatcher("areashop <'shop'> sold");
    }

    public SoldRegionEvent event;
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
        return new BukkitScriptEntryData(new PlayerTag(event.getOldBuyer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("shop")) {
            return areaShop;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onAreaShopEvent(SoldRegionEvent event) {
        areaShop = new AreaShopTag(event.getRegion());
        this.event = event;
        fire(event);
    }
}
