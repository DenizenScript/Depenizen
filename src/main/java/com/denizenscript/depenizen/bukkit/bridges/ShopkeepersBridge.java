package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperCreatedScriptEvent;
import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperTradeCompletedScriptEvent;
import com.denizenscript.depenizen.bukkit.events.shopkeepers.ShopKeeperTradeInitiatedScriptEvent;
import com.denizenscript.depenizen.bukkit.objects.shopkeepers.ShopKeeperTag;
import com.denizenscript.depenizen.bukkit.properties.shopkeepers.ShopKeepersEntityExtensions;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;

public class ShopkeepersBridge extends Bridge {

    public static ShopkeepersBridge instance;

    @Override
    public void init() {
        instance = this;
        ObjectFetcher.registerWithObjectFetcher(ShopKeeperTag.class, ShopKeeperTag.tagProcessor);
        ScriptEvent.registerScriptEvent(ShopKeeperCreatedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ShopKeeperTradeCompletedScriptEvent.class);
        ScriptEvent.registerScriptEvent(ShopKeeperTradeInitiatedScriptEvent.class);
        ShopKeepersEntityExtensions.register();
    }

    public static ListTag tradingRecipeToList(TradingRecipe tradingRecipe) {
        ListTag recipe = new ListTag(3);
        recipe.addObject(new ItemTag(tradingRecipe.getItem1().copy()));
        recipe.addObject(new ItemTag(tradingRecipe.getItem2() != null ? tradingRecipe.getItem2().copy() : null));
        recipe.addObject(new ItemTag(tradingRecipe.getResultItem().copy()));
        return recipe;
    }
}
