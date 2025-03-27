package com.denizenscript.depenizen.bukkit.events.chestshop;

import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChestShopShopDeletedScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // chestshop shop deleted
    //
    // @Triggers when a chestshop is deleted.
    //
    // @Context
    // <context.container> returns a LocationTag of the container attached to the shop, if any.
    // <context.sign> returns a LocationTag of the sign.
    //
    // @Plugin Depenizen, ChestShop
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ChestShopShopDeletedScriptEvent() {
        registerCouldMatcher("chestshop shop deleted");
    }

    public ShopDestroyedEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getDestroyer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "container" -> event.getContainer() == null ? null : new LocationTag(event.getContainer().getLocation());
            case "sign" -> new LocationTag(event.getSign().getLocation());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onChestShopDeleted(ShopDestroyedEvent event) {
        this.event = event;
        fire(event);
    }
}
