package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.bridges.ShopkeepersBridge;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeperTag;
import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopKeeperTradeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // shopkeeper trade
    //
    // @Cancellable true
    //
    // @Triggers when a trade with a shopkeeper is completed.
    //
    // @Context
    // <context.recipe> Returns a ListTag(ItemTag) of the recipe for this trade.
    // <context.shopkeeper> Returns the ShopKeeperTag of the ShopKeeper that the trade occurred with.
    //
    // @Plugin Depenizen, ShopKeepers
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ShopKeeperTradeScriptEvent() {
        registerCouldMatcher("shopkeeper trade");
    }

    public ShopkeeperTradeEvent event;

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(event.getPlayer());
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "recipe" -> ShopkeepersBridge.wrapTradingRecipe(event.getTradingRecipe());
            case "shopkeeper" -> new ShopKeeperTag(event.getShopkeeper());
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onShopKeeperTrade(ShopkeeperTradeEvent event) {
        this.event = event;
        fire(event);
    }
}
