package com.denizenscript.depenizen.bukkit.objects.shopkeepers;

import com.denizenscript.depenizen.bukkit.bridges.ShopkeepersBridge;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.denizenscript.denizen.objects.dEntity;
import com.denizenscript.denizen.objects.dItem;
import com.denizenscript.denizencore.objects.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.tags.core.EscapeTags;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopKeeper implements ObjectTag {

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
        return ((ShopkeepersPlugin) ShopkeepersBridge.instance.plugin).getShopkeeperRegistry().isShopkeeper(entity.getBukkitEntity());
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
            Debug.echoError("Shopkeeper referenced is null!");
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
    public ObjectTag setPrefix(String prefix) {
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
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the Shopkeeper is active.
        // @Plugin Depenizen, ShopKeepers
        // -->
        if (attribute.startsWith("is_active")) {
            return new ElementTag(shopkeeper.isActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.is_ui_active>
        // @returns ElementTag(Boolean)
        // @description
        // Returns whether the Shopkeeper is currently in a trade.
        // @Plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("is_ui_active")) {
            return new ElementTag(shopkeeper.isUIActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.trades>
        // @returns ListTag(dList)
        // @description
        // Returns an escaped ListTag of the Shopkeeper's trades.
        // NOTE: see '!language Property Escaping'.
        // @Plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("trades") || attribute.startsWith("recipes")) {
            ListTag trades = new ListTag();
            for (TradingRecipe trade : shopkeeper.getTradingRecipes(null)) {
                ListTag recipe = wrapTradingRecipe(trade);
                trades.add(EscapeTags.escape(recipe.identify()));
            }
            return trades.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for this ShopKeeper.
        // @Plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("entity")) {
            return getDenizenEntity().getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.type>
        // @returns ElementTag
        // @description
        // Always returns 'ShopKeeper' for ShopKeeper objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @Plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("type")) {
            return new ElementTag("ShopKeeper").getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    public static ListTag wrapTradingRecipe(TradingRecipe tradingRecipe) {
        ItemStack item1 = tradingRecipe.getItem1();
        ItemStack item2 = tradingRecipe.getItem2();
        ItemStack resultItem = tradingRecipe.getResultItem();

        ListTag recipe = new ListTag();
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
