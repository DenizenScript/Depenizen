package com.denizenscript.depenizen.bukkit.objects.shopkeepers;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.denizenscript.depenizen.bukkit.bridges.ShopkeepersBridge;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopKeeperTag implements ObjectTag {

    // <--[ObjectType]
    // @name ShopKeeperTag
    // @prefix shopkeeper
    // @base ElementTag
    // @format
    // The identity format for shopkeepers is <uuid>
    // For example, 'shopkeeper@1234-1234-1234'.
    //
    // @plugin Depenizen, Shopkeepers
    // @description
    // A ShopKeeperTag represents a ShopKeeper entity in the world.
    //
    // -->

    @Fetchable("shopkeeper")
    public static ShopKeeperTag valueOf(String string, TagContext context) {
        if (string.startsWith("shopkeeper@")) {
            string = string.substring("shopkeeper@".length());
        }
        try {
            UUID uuid = UUID.fromString(string);
            Shopkeeper keeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByUniqueId(uuid);
            if (keeper == null) {
                return null;
            }
            return new ShopKeeperTag(keeper);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static boolean matches(String string) {
        return valueOf(string, CoreUtilities.noDebugContext) != null;
    }

    public static boolean isShopKeeper(EntityTag entity) {
        if (entity == null) {
            return false;
        }
        return ((ShopkeepersPlugin) ShopkeepersBridge.instance.plugin).getShopkeeperRegistry().isShopkeeper(entity.getBukkitEntity());
    }

    public static ShopKeeperTag fromEntity(EntityTag entity) {
        if (!isShopKeeper(entity)) {
            return null;
        }
        return new ShopKeeperTag(ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByEntity(entity.getBukkitEntity()));
    }

    public ShopKeeperTag(Shopkeeper shopkeeper) {
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

    public EntityTag getDenizenEntity() {
        return new EntityTag(getBukkitEntity());
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
    public boolean isUnique() {
        return true;
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
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.is_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper is active.
        // -->
        if (attribute.startsWith("is_active")) {
            return new ElementTag(shopkeeper.isActive()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.is_ui_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper UI is currently active (may be false when the UI is about to be closed).
        // -->
        else if (attribute.startsWith("is_ui_active")) {
            return new ElementTag(shopkeeper.isUIActive()).getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.trades>
        // @returns ListTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns a ListTag of the Shopkeeper's trades (as sub-lists).
        // -->
        else if (attribute.startsWith("trades") || attribute.startsWith("recipes")) {
            ListTag trades = new ListTag();
            for (TradingRecipe trade : shopkeeper.getTradingRecipes(null)) {
                ListTag recipe = wrapTradingRecipe(trade);
                trades.addObject(recipe);
            }
            return trades.getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the EntityTag for this ShopKeeper.
        // -->
        else if (attribute.startsWith("entity")) {
            return getDenizenEntity().getObjectAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.owner>
        // @returns PlayerTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the player that owns this ShopKeeper, if any.
        // -->
        else if (attribute.startsWith("owner")) {
            if (shopkeeper instanceof PlayerShopkeeper) {
                return new PlayerTag(((PlayerShopkeeper) shopkeeper).getOwnerUUID()).getObjectAttribute(attribute.fulfill(1));
            }
            return null;
        }

        return null;
    }

    public static ListTag wrapTradingRecipe(TradingRecipe tradingRecipe) {
        ItemStack item1 = tradingRecipe.getItem1();
        ItemStack item2 = tradingRecipe.getItem2();
        ItemStack resultItem = tradingRecipe.getResultItem();

        ListTag recipe = new ListTag();
        recipe.addObject(new ItemTag(item1));
        recipe.addObject(new ItemTag(item2));
        recipe.addObject(new ItemTag(resultItem));
        return recipe;
    }
}
