package com.denizenscript.depenizen.bukkit.events.areashop;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.areashop.AreaShopTag;
import me.wiefferink.areashop.events.notify.RentedRegionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopRentedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // areashop <'shop'> rented
    //
    // @Triggers when an AreaShop is rented.
    //
    // @Context
    // <context.shop> returns the AreaShop that's being rented.
    // <context.extended> returns true if the region has been extended, false if this is the first time buying the region.
    //
    // @Plugin Depenizen, AreaShop
    //
    // @Player Always (the renter).
    //
    // @Group Depenizen
    //
    // -->

    public AreaShopRentedScriptEvent() {
        registerCouldMatcher("areashop <'shop'> rented");
    }

    public RentedRegionEvent event;
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
        return new BukkitScriptEntryData(new PlayerTag(event.getRegion().getRenter()), null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("shop")) {
            return areaShop;
        }
        if (name.equals("extended")) {
            return new ElementTag(event.hasExtended());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onAreaShopEvent(RentedRegionEvent event) {
        areaShop = new AreaShopTag(event.getRegion());
        this.event = event;
        fire(event);
    }
}
