package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeperTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopKeeperTradeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // shopkeeper trade
    //
    // @Regex ^on shopkeeper trade$
    //
    // @Cancellable true
    //
    // @Triggers when a trade with a shopkeeper is completed.
    //
    // @Context
    // <context.recipe> Returns the recipe for this trade.
    // <context.shopkeeper> Returns the ShopKeeper that the trade occurred with.
    //
    // @Plugin Depenizen, ShopKeepers
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public ShopKeeperTradeScriptEvent() {
    }

    public ShopkeeperTradeEvent event;
    public PlayerTag player;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return path.eventLower.startsWith("shopkeeper trade");
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.startsWith("recipe")) {
            return ShopKeeperTag.wrapTradingRecipe(event.getTradingRecipe());
        }
        else if (name.equals("shopkeeper")) {
            return new ShopKeeperTag(event.getShopkeeper());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onShopKeeperTrade(ShopkeeperTradeEvent event) {
        player = new PlayerTag(event.getPlayer());
        this.event = event;
        fire(event);
    }
}
