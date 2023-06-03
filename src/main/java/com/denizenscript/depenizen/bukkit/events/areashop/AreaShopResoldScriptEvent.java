package com.denizenscript.depenizen.bukkit.events.areashop;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import me.wiefferink.areashop.events.notify.ResoldRegionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopResoldScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // areashop <'shop'> resold
    //
    // @Triggers when an AreaShop is resold.
    //
    // @Context
    // <context.shop> returns the AreaShop that's being resold.
    // <context.seller> returns the player that has resold the shop.
    //
    // @Plugin Depenizen, AreaShop
    //
    // @Player Always (the buyer).
    //
    // @Group Depenizen
    //
    // -->

    public AreaShopResoldScriptEvent() {
        registerCouldMatcher("areashop <'shop'> resold");
    }

    public ResoldRegionEvent event;
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
        return new BukkitScriptEntryData(new PlayerTag(event.getRegion().getBuyer()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("shop")) {
            return areaShop;
        }
        if (name.equals("seller")) {
            return new PlayerTag(event.getFromPlayer());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onAreaShopEvent(ResoldRegionEvent event) {
        areaShop = new AreaShopTag(event.getRegion());
        this.event = event;
        fire(event);
    }
}
