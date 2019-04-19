package com.denizenscript.depenizen.bukkit.events.areashop;

import me.wiefferink.areashop.events.notify.UnrentedRegionEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// <--[event]
// @Events
// areashop shop expires
// areashop <shop> expires
//
// @Regex ^on areashop [^\s]+ expires$
//
// @Cancellable false
//
// @Triggers when an AreaShop's rent expires.
//
// @Context
// <context.shop> Returns the AreaShop that's expiring.
//
// @Plugin DepenizenBukkit, AreaShop
//
// -->

public class AreaShopExpiresScriptEvent extends BukkitScriptEvent implements Listener {

    public AreaShopExpiresScriptEvent() {
        instance = this;
    }

    public static AreaShopExpiresScriptEvent instance;
    public UnrentedRegionEvent event;
    public dAreaShop areaShop;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return s.startsWith("areashop") && CoreUtilities.xthArgEquals(2, lower, "expires");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String shopName = path.eventArgLowerAt(2).replace("areashop@", "");
        if (shopName.equals("shop")) {
            return true;
        }
        dAreaShop shop = dAreaShop.valueOf(shopName);
        return shop != null && shop.equals(areaShop);
    }

    @Override
    public String getName() {
        return "AreaShopExpires";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(new dPlayer(event.getOldRenter()), null);
    }

    @Override
    public dObject getContext(String name) {
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
        areaShop = new dAreaShop(event.getRegion());
        this.event = event;
        fire(event);
    }
}
