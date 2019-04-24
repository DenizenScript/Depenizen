package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeper;
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
    // <context.shopkeeper> Returns the ShopKeeper that the trade occured with.
    //
    // @Plugin DepenizenBukkit, ShopKeepers
    //
    // -->

    public ShopKeeperTradeScriptEvent() {
        instance = this;
    }

    public static ShopKeeperTradeScriptEvent instance;
    public ShopkeeperTradeEvent event;
    public ShopKeeper keeper;
    public dPlayer player;
    public dList recipe;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("shopkeeper trade");
    }

    @Override
    public boolean matches(ScriptPath path) {
        return true;
    }

    @Override
    public String getName() {
        return "ShopKeeperTrade";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.startsWith("recipe")) {
            return recipe;
        }
        else if (name.equals("shopkeeper")) {
            return keeper;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onShopKeeperTrade(ShopkeeperTradeEvent event) {
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        keeper = new ShopKeeper(event.getShopkeeper());
        recipe = ShopKeeper.wrapTradingRecipe(event.getTradingRecipe());
        this.event = event;
        fire(event);
    }
}
