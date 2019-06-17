package com.denizenscript.depenizen.bukkit.objects.shopkeepers;

import com.denizenscript.depenizen.bukkit.support.Support;
import com.denizenscript.depenizen.bukkit.bridges.ShopkeepersSupport;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.tags.core.EscapeTags;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopKeeper implements dObject {

    public static ShopKeeper valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("shopkeeper")
    public static ShopKeeper valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            UUID uuid = UUID.fromString(string);
            Shopkeeper keeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByUniqueId(uuid);
            if (keeper == null) {
                return null;
            }
            return new ShopKeeper(keeper);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static boolean isShopKeeper(dEntity entity) {
        if (entity == null) {
            return false;
        }
        ShopkeepersPlugin plugin = Support.getPlugin(ShopkeepersSupport.class);
        return plugin != null && plugin.getShopkeeperRegistry().isShopkeeper(entity.getBukkitEntity());
    }

    public static ShopKeeper fromEntity(dEntity entity) {
        if (!isShopKeeper(entity)) {
            return null;
        }
        return new ShopKeeper(ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByEntity(entity.getBukkitEntity()));
    }

    public ShopKeeper(Shopkeeper shopkeeper) {
        if (shopkeeper != null) {
            this.shopkeeper = shopkeeper;
        }
        else {
            dB.echoError("Shopkeeper referenced is null!");
        }
    }

    private String prefix;
    Shopkeeper shopkeeper;

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public dEntity getDenizenEntity() {
        return new dEntity(getBukkitEntity());
    }

    public Entity getBukkitEntity() {
        if (shopkeeper.getShopObject() instanceof EntityShopObject) {
            return ((EntityShopObject) shopkeeper.getShopObject()).getEntity();
        }
        return null;
    }

    @Override
    public dObject setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>' ";
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "ShopKeeper";
    }

    @Override
    public String identify() {
        return "shopkeeper@" + shopkeeper.getUniqueId();
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public String toString() {
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.is_active>
        // @returns Element(Boolean)
        // @description
        // Returns whether the Shopkeeper is active.
        // @Plugin DepenizenBukkit, ShopKeepers
        // -->
        if (attribute.startsWith("is_active")) {
            return new Element(shopkeeper.isActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.is_ui_active>
        // @returns Element(Boolean)
        // @description
        // Returns whether the Shopkeeper is currently in a trade.
        // @Plugin DepenizenBukkit, ShopKeepers
        // -->
        else if (attribute.startsWith("is_ui_active")) {
            return new Element(shopkeeper.isUIActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.trades>
        // @returns dList(dList)
        // @description
        // Returns an escaped dList of the Shopkeeper's trades.
        // NOTE: see '!language Property Escaping'.
        // @Plugin DepenizenBukkit, ShopKeepers
        // -->
        else if (attribute.startsWith("trades") || attribute.startsWith("recipes")) {
            dList trades = new dList();
            for (TradingRecipe trade : shopkeeper.getTradingRecipes(null)) {
                dList recipe = wrapTradingRecipe(trade);
                trades.add(EscapeTags.escape(recipe.identify()));
            }
            return trades.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for this ShopKeeper.
        // @Plugin DepenizenBukkit, ShopKeepers
        // -->
        else if (attribute.startsWith("entity")) {
            return getDenizenEntity().getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.type>
        // @returns Element
        // @description
        // Always returns 'ShopKeeper' for ShopKeeper objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin DepenizenBukkit, ShopKeepers
        // -->
        else if (attribute.startsWith("type")) {
            return new Element("ShopKeeper").getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    public static dList wrapTradingRecipe(TradingRecipe tradingRecipe) {
        ItemStack item1 = tradingRecipe.getItem1();
        ItemStack item2 = tradingRecipe.getItem2();
        ItemStack resultItem = tradingRecipe.getResultItem();

        dList recipe = new dList();
        recipe.add(wrapTradeItem(item1).identify());
        recipe.add(wrapTradeItem(item2).identify());
        recipe.add(wrapTradeItem(resultItem).identify());
        return recipe;
    }

    private static dItem wrapTradeItem(ItemStack itemStack) {
        if (itemStack != null) {
            return new dItem(itemStack);
        }
        else {
            return new dItem(Material.AIR);
        }
    }
}
