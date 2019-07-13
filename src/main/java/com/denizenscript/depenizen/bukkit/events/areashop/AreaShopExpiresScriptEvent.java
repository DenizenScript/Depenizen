package com.denizenscript.depenizen.bukkit.events.areashop;

import me.wiefferink.areashop.events.notify.UnrentedRegionEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.areashop.dAreaShop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaShopExpiresScriptEvent extends BukkitScriptEvent implements Listener {

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
    // @Plugin Depenizen, AreaShop
    //
    // -->

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
        areaShop = new dAreaShop(event.getRegion());
        this.event = event;
        fire(event);
    }
}
