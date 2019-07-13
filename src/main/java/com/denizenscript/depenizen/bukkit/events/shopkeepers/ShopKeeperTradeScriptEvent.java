package com.denizenscript.depenizen.bukkit.events.shopkeepers;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
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
    // @Plugin Depenizen, ShopKeepers
    //
    // -->

    public ShopKeeperTradeScriptEvent() {
        instance = this;
    }

    public static ShopKeeperTradeScriptEvent instance;
    public ShopkeeperTradeEvent event;
    public ShopKeeper keeper;
    public PlayerTag player;
    public ListTag recipe;

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
    public ObjectTag getContext(String name) {
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
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer());
        keeper = new ShopKeeper(event.getShopkeeper());
        recipe = ShopKeeper.wrapTradingRecipe(event.getTradingRecipe());
        this.event = event;
        fire(event);
    }
}
