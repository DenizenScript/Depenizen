package net.gnomeffinway.depenizen.objects.shopkeepers;

import com.nisovin.shopkeepers.Shopkeeper;
import com.nisovin.shopkeepers.ShopkeepersPlugin;
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
            Shopkeeper keeper = ShopkeepersPlugin.getInstance().getShopkeeper(uuid);
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
        return ShopkeepersPlugin.getInstance().isShopkeeper(entity.getBukkitEntity());
    }

    public static ShopKeeper fromEntity(dEntity entity) {
        if (!isShopKeeper(entity)) {
            return null;
        }
        return new ShopKeeper(ShopkeepersPlugin.getInstance().getShopkeeperByEntity(entity.getBukkitEntity()));
    }

    public ShopKeeper(Shopkeeper shopkeeper) {
        if (shopkeeper != null) {
            this.shopkeeper = shopkeeper;
            this.entity = dEntity.getEntityForID(shopkeeper.getUniqueId());
        }
        else {
            dB.echoError("Shopkeeper referenced is null!");
        }
    }

    private String prefix;
    Shopkeeper shopkeeper;
    Entity entity;

    public Shopkeeper getShopkeeper() {
        return shopkeeper;
    }

    public dEntity getDenizenEntity() {
        return new dEntity(entity);
    }

    public Entity getBukkitEntity() {
        return entity;
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.is_active>
        // @returns Element(Boolean)
        // @description
        // Returns whether the Shopkeeper is active.
        // @plugin Depenizen, ShopKeepers
        // -->
        if (attribute.startsWith("is_active")) {
            return new Element(shopkeeper.isActive()).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.is_ui_active>
        // @returns Element(Boolean)
        // @description
        // Returns whether the Shopkeeper is currently in a trade.
        // @plugin Depenizen, ShopKeepers
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
        // @plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("trades") || attribute.startsWith("recipes")) {
            dList trades = new dList();
            for (ItemStack[] trade : shopkeeper.getRecipes()) {
                dList recipe = new dList();
                for (ItemStack item : trade) {
                    if (item != null) {
                        recipe.add(new dItem(item).identify());
                    }
                    else {
                        recipe.add(new dItem(Material.AIR).identify());
                    }
                }
                trades.add(EscapeTags.Escape(recipe.identify()));
            }
            return trades.getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.entity>
        // @returns dEntity
        // @description
        // Returns the dEntity for this ShopKeeper.
        // @plugin Depenizen, ShopKeepers
        // -->
        else if (attribute.startsWith("entity")) {
            return new dEntity(entity).getAttribute(attribute.fulfill(1));
        }

        // <--[tag]
        // @attribute <shopkeeper@shopkeeper.type>
        // @returns Element
        // @description
        // Always returns 'ShopKeeper' for ShopKeeper objects. All objects fetchable by the Object Fetcher will return the
        // type of object that is fulfilling this attribute.
        // @plugin Depenizen, MythicMobs
        // -->
        else if (attribute.startsWith("type")) {
            return new Element("ShopKeeper").getAttribute(attribute.fulfill(1));
        }

        return null;
    }
}
