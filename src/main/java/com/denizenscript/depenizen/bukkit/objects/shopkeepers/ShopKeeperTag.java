package com.denizenscript.depenizen.bukkit.objects.shopkeepers;

import com.denizenscript.denizen.objects.PlayerTag;
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
import com.denizenscript.denizencore.tags.core.EscapeTagBase;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopKeeperTag implements ObjectTag {

    // <--[language]
    // @name ShopKeeperTag Objects
    // @group Depenizen Object Types
    // @plugin Depenizen, Shopkeepers
    // @description
    // A ShopKeeperTag represents a ShopKeeper entity in the world.
    //
    // For format info, see <@link language shopkeeper@>
    //
    // -->

    // <--[language]
    // @name shopkeeper@
    // @group Depenizen Object Fetcher Types
    // @plugin Depenizen, Shopkeepers
    // @description
    // shopkeeper@ refers to the 'object identifier' of a ShopKeeperTag. The 'shopkeeper@' is notation for Denizen's Object
    // Fetcher. The constructor for a ShopKeeperTag is <uuid>
    // For example, 'shopkeeper@1234-1234-1234'.
    //
    // For general info, see <@link language ShopKeeperTag Objects>
    //
    // -->

    public static ShopKeeperTag valueOf(String string) {
        return valueOf(string, null);
    }

    @Fetchable("shopkeeper")
    public static ShopKeeperTag valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
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
        return valueOf(string) != null;
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
        // @attribute <ShopKeeperTag.is_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper is active.
        // -->
        if (attribute.startsWith("is_active")) {
            return new ElementTag(shopkeeper.isActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.is_ui_active>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns whether the Shopkeeper is currently in a trade.
        // -->
        else if (attribute.startsWith("is_ui_active")) {
            return new ElementTag(shopkeeper.isUIActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.trades>
        // @returns ListTag)
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns a ListTag of the Shopkeeper's trades (as escaped sub-lists).
        // NOTE: see '!language Property Escaping'.
        // -->
        else if (attribute.startsWith("trades") || attribute.startsWith("recipes")) {
            ListTag trades = new ListTag();
            for (TradingRecipe trade : shopkeeper.getTradingRecipes(null)) {
                ListTag recipe = wrapTradingRecipe(trade);
                trades.add(EscapeTagBase.escape(recipe.identify()));
            }
            return trades.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.entity>
        // @returns EntityTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Returns the EntityTag for this ShopKeeper.
        // -->
        else if (attribute.startsWith("entity")) {
            return getDenizenEntity().getAttribute(attribute.fulfill(1));
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
                return new PlayerTag(((PlayerShopkeeper) shopkeeper).getOwnerUUID()).getAttribute(attribute.fulfill(1));
            }
            return null;
        }

        // <--[tag]
        // @attribute <ShopKeeperTag.type>
        // @returns ElementTag
        // @plugin Depenizen, ShopKeepers
        // @description
        // Always returns 'ShopKeeper' for ShopKeeper objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
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

    private static ItemTag wrapTradeItem(ItemStack itemStack) {
        if (itemStack != null) {
            return new ItemTag(itemStack);
        }
        else {
            return new ItemTag(Material.AIR);
        }
    }
}
