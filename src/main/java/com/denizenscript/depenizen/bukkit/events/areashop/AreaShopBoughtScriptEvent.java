package com.denizenscript.depenizen.bukkit.events.areashop;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import me.wiefferink.areashop.events.notify.BoughtRegionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopBoughtScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // areashop <'shop'> bought
    //
    // @Triggers when an AreaShop is bought.
    //
    // @Context
    // <context.shop> returns the AreaShop that's being bought.
    //
    // @Plugin Depenizen, AreaShop
    //
    // @Player Always (the buyer).
    //
    // @Group Depenizen
    //
    // -->

    public AreaShopBoughtScriptEvent() {
        registerCouldMatcher("areashop <'shop'> bought");
    }

    public BoughtRegionEvent event;
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
        return super.getContext(name);
    }

    @EventHandler
    public void onAreaShopEvent(BoughtRegionEvent event) {
        areaShop = new AreaShopTag(event.getRegion());
        this.event = event;
        fire(event);
    }
}
